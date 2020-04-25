package septogeddon.pear.library;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.plugin.Listener;
import septogeddon.pear.api.Bridge;
import septogeddon.pear.api.Connection;
import septogeddon.pear.api.ConnectionObject;
import septogeddon.pear.api.Network;
import septogeddon.pear.api.Packet;
import septogeddon.pear.api.TypeTranslator;
import septogeddon.pear.library.RequestQueue.Queue;
import septogeddon.pear.packets.PacketConnectionClosed;
import septogeddon.pear.packets.PacketConnectionOpen;
import septogeddon.pear.packets.PacketDeliveredThrowable;
import septogeddon.pear.packets.PacketFieldGet;
import septogeddon.pear.packets.PacketFieldSet;
import septogeddon.pear.packets.PacketMethodInvocation;
import septogeddon.pear.packets.PacketReferenceClosed;
import septogeddon.pear.packets.PacketRequestService;

public class NetworkImpl implements Network, Listener {

	private Bridge bridge;
	private RequestQueue requests = new RequestQueue();
	private List<ConnectionObject> connections = new ArrayList<>();
	private Map<Class<?>, TypeTranslator> translators = new HashMap<>();
	private List<ReflectedObject> services = new ArrayList<>();
	private List<ClassAlias> aliases = new ArrayList<>();
	private long lastId = 1;

	public NetworkImpl(Bridge pear) {
		bridge = pear;
	}

	@Override
	public void close() {
		long[] ids = new long[connections.size()];
		for (int i = ids.length - 1; i >= 0; i--) {
			ids[i] = connections.remove(i).ConnectionObject$getConnection().getObjectId();
		}
		sendPacket(new PacketConnectionClosed(ids));
	}

	@Override
	public void closeConnection(long id) {
		connections.removeIf(el -> el.ConnectionObject$getConnection().getObjectId() == id);
		sendPacket(new PacketConnectionClosed(id));
	}

	protected String convertCrossOver(Class<?> cl) {
		for (ClassAlias alias : aliases) {
			if (alias.getOriginal().equals(cl)) {
				return alias.getAlias().getName();
			}
		}
		return cl.getName();
	}

	protected Class<?> convertCrossOver(String className) throws ClassNotFoundException {
		for (ClassAlias alias : aliases) {
			if (alias.getAlias().getName().equals(className)) {
				return alias.getOriginal();
			}
		}
		return Class.forName(className);
	}

	@Override
	public <T> T createConnection(String serviceId, Class<T> interf) {
		PacketRequestService packet = sendPacket(new PacketRequestService(serviceId));
		return this.registerConnection(packet.getObjectId(), interf);
	}

	@Override
	public void dispatchPacket(Packet packet) {
		if (packet.getMode() == Packet.MODE_SERVER_TO_CLIENT) {
			// act as client
			requests.finish(packet);
		} else {
			try {
				if (packet instanceof PacketRequestService) {
					PacketRequestService serv = (PacketRequestService) packet;
					ReflectedObject obj = getService(serv.getService());
					if (obj == null) {
						throw new NullPointerException("no such service");
					}
					serv.setObjectId(obj.getId());
					sendPacket(serv.handleConversion()); // callback packet
					return;
				}
				if (packet instanceof PacketConnectionOpen) {
					PacketConnectionOpen open = (PacketConnectionOpen) packet;
					Class<?> interf = this.convertCrossOver(open.getClassName());
					long id = open.getObjectId();
					this.registerConnection(id, interf);
					sendPacket(open.handleConversion());
					return;
				}
				if (packet instanceof PacketFieldSet) {
					PacketFieldSet set = (PacketFieldSet) packet;
					ReflectedObject obj = getExistingService(set.getObjectId());
					if (obj == null) {
						throw new NullPointerException("no such object preserved");
					}
					obj.setField(set.getField(), unwrap(set.getValue()));
					sendPacket(set.handleConversion());
					return;
				}
				if (packet instanceof PacketFieldGet) {
					PacketFieldGet get = (PacketFieldGet) packet;
					ReflectedObject obj = getExistingService(get.getObjectId());
					if (obj == null)
						throw new NullPointerException("no such object preserved");
					Field field = obj.findField(get.getField());
					get.setValue(wrap(obj, field.get(obj.getValue()), get.getHintClassName()));
					sendPacket(get.handleConversion());
					return;
				}
				if (packet instanceof PacketMethodInvocation) {
					PacketMethodInvocation met = (PacketMethodInvocation) packet;
					ReflectedObject obj = getExistingService(met.getObjectId());
					if (obj == null)
						throw new NullPointerException("no such object preserved");
					Class<?>[] par = new Class<?>[met.getParameters().length];
					for (int i = 0; i < par.length; i++) {
						par[i] = convertCrossOver(met.getParameters()[i]);
					}
					Object[] args = met.getArguments();
					if (args != null) {
						for (int i = 0; i < args.length; i++) {
							args[i] = unwrap(args[i]);
						}
					}
					Method method = obj.findMethod(met.getMethod(), par);
					met.setValue(wrap(obj, method.invoke(obj.getValue(), args), met.getHintClassName()));
					sendPacket(met.handleConversion());
					return;
				}
				if (packet instanceof PacketConnectionClosed) {
					long[] closed = ((PacketConnectionClosed) packet).getObjectIds();
					for (int i = services.size() - 1; i >= 0; i--) {
						ReflectedObject obj = services.get(i);
						for (long l : closed) {
							if (obj.getId() == l) {
								services.remove(i);
								for (ReflectedObject o : obj.getUsedIds()) {
									services.remove(o);
								}
								break;
							}
						}
					}
					sendPacket(packet.handleConversion());
				}
			} catch (Throwable t) {
				sendPacket(new PacketDeliveredThrowable(t).handleConversion().setRequestId(packet.getRequestId()));
			}
		}
	}

	public void flush() {
		for (int i = services.size() - 1; i >= 0; i++) {
			ReflectedObject obj = services.get(i);
			if (obj.shouldFlush()) {
				services.remove(i);
				sendPacket(new PacketReferenceClosed(obj.getId()));
			}
		}
	}

	@Override
	public ConnectionObject getExistingConnection(long objectId) {
		for (int i = connections.size() - 1; i >= 0; i--) {
			ConnectionObject obj = connections.get(i);
			Connection conn = obj.ConnectionObject$getConnection();
			if (conn.getObjectId() == objectId) {
				return obj;
			}
		}
		return null;
	}

	public ReflectedObject getExistingService(long id) {
		for (int i = services.size() - 1; i >= 0; i--) {
			ReflectedObject obj = services.get(i);
			if (obj.getId() == id)
				return obj;
		}
		return null;
	}

	public ReflectedObject getService(String serviceId) {
		for (int i = services.size() - 1; i >= 0; i--) {
			ReflectedObject obj = services.get(i);
			if (serviceId.equals(obj.getServiceName())) {
				return obj;
			}
		}
		return null;
	}

	public TypeTranslator getTranslator(Class<?> cl) {
		return translators.get(cl);
	}

	protected long nextId() {
		return lastId++;
	}

	@SuppressWarnings("unchecked")
	public <T> T registerConnection(long id, Class<T> interf) {
		Connection con = new ConnectionImpl(this, id);
		Map<String, Var<?>> variables = new HashMap<>();
		for (Method m : interf.getMethods()) {
			if (Var.class.equals(m.getReturnType())) {
				variables.put(m.getName(), new Var<>(con, m.getName(),
						((ParameterizedType) m.getGenericReturnType()).getActualTypeArguments()[0]));
			}
		}
		Object obj = Proxy.newProxyInstance(interf.getClassLoader(),
				interf.equals(ConnectionObject.class) ? new Class<?>[] { ConnectionObject.class }
						: new Class<?>[] { interf, ConnectionObject.class },
				(proxy, method, args) -> {
					if (method.equals(ConnectionObject.GETTER_METHOD)) {
						return con;
					}
					if (Var.class.equals(method.getReturnType())) {
						return variables.get(method.getName());
					}
					Class<?>[] parm = method.getParameterTypes();
					String[] pars = new String[parm.length];
					for (int i = 0; i < parm.length; i++) {
						pars[i] = convertCrossOver(parm[i]);
					}
					Object val = con.invokeMethod(method.getName(), pars, args, method.getReturnType());
					return unwrap(val);
				});
		connections.add((ConnectionObject) obj);
		return (T) obj;
	}

	@Override
	public void registerInterface(Class<?> cl, Class<?> alias) {
		unregisterInterface(cl);
		aliases.add(new ClassAlias(cl, alias));
	}

	@Override
	public ReflectedObject registerService(String serviceId, Object obj) {
		ReflectedObject object;
		services.add(object = new ReflectedObject(obj, nextId(), serviceId));
		return object;
	}

	@Override
	public void registerTypeTranslator(TypeTranslator tr, Class<?>... cl) {
		for (Class<?> c : cl)
			translators.put(c, tr);
	}

	@Override
	public <T extends Packet> T sendPacket(T packet) {
		Queue<T> queue = requests.queue(packet);
		bridge.send(packet);
		return queue.get();
	}

	@Override
	public void shutdown() {
		close();
	}

	@Override
	public void start() {
	}

	@Override
	public void unregisterInterface(Class<?> cl) {
		aliases.removeIf(el -> el.getOriginal().equals(cl));
	}

	public Object unwrap(Object obj) {
		if (obj != null) {
			TypeTranslator tr = translators.get(obj.getClass());
			if (tr != null)
				obj = tr.unwrap(obj);
		}
		if (obj instanceof ReferencedObject) {
			obj = this.getExistingConnection(((ReferencedObject) obj).getObjectId());
		}
		return obj;
	}

	public Object wrap(Object value, String hint) {
		value = wrap(null, value, hint);
		return value;
	}

	public Object wrap(ReflectedObject ref, Object obj, String hint) {
		if (obj instanceof ConnectionObject) {
			Connection conn = ((ConnectionObject) obj).ConnectionObject$getConnection();
			if (conn.getNetwork() == this) { // same network, same ids
				obj = new ReferencedObject(conn.getObjectId());
			}
		}
		if (obj != null && !(obj instanceof Serializable)) {
			ReflectedObject object = this.registerService(null, obj);
			if (ref != null) {
				ref.getUsedIds().add(object);
			}
			sendPacket(new PacketConnectionOpen(hint, object.getId()));
			obj = new ReferencedObject(object.getId());
		}
		if (obj != null) {
			TypeTranslator tr = translators.get(obj.getClass());
			if (tr != null)
				obj = tr.wrap(obj);
		}
		return obj;
	}

}
