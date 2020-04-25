package septogeddon.pear.library;

import septogeddon.pear.api.Connection;
import septogeddon.pear.api.Network;
import septogeddon.pear.packets.PacketFieldGet;
import septogeddon.pear.packets.PacketFieldSet;
import septogeddon.pear.packets.PacketMethodInvocation;

public class ConnectionImpl implements Connection {
	private long objectId;
	private Network network;
	public ConnectionImpl(Network net,long id) {
		network = net;
		objectId = id;
	}
	@Override
	public long getObjectId() {
		return objectId;
	}

	@Override
	public Network getNetwork() {
		return network;
	}
	@Override
	public Object invokeMethod(String name, String[] params, Object[] args, Class<?> returnType) {
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				args[i] = network.wrap(args[i], params[i]);
			}
		}
		PacketMethodInvocation packet = new PacketMethodInvocation(objectId,name,params,args, returnType);
		return network.unwrap(network.sendPacket(packet).getValue());
	}
	@Override
	public Object getField(String info,Class<?> type) {
		return network.unwrap(network.sendPacket(new PacketFieldGet(objectId, info, type)).getValue());
	}
	@Override
	public void setField(String info, Object value, Class<?> type) {
		value = network.wrap(value, type.getName());
		network.sendPacket(new PacketFieldSet(info, value, objectId));
	}
}
