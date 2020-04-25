package septogeddon.pear.api;

import septogeddon.pear.library.ReflectedObject;

public interface Network extends AutoCloseable {

	public void start();
	public void shutdown();
	public <T> T createConnection(String serviceId,Class<T> interf);
	public ConnectionObject getExistingConnection(long objectId);
	public ReflectedObject registerService(String serviceId,Object obj);
	public void registerInterface(Class<?> cl,Class<?> interf);
	public void unregisterInterface(Class<?> cl);
	public void registerTypeTranslator(TypeTranslator tr,Class<?>... cl);
	public <T extends Packet> T sendPacket(T packet);
	public Object unwrap(Object obj);
	public Object wrap(Object value,String hint);
	public void dispatchPacket(Packet packet);
	public void close();
	public void closeConnection(long id);
	
}
