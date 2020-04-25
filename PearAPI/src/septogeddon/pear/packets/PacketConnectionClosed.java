package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketConnectionClosed extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4238311520675254517L;
	private long[] objectId;
	public PacketConnectionClosed(long... id) {
		this.objectId = id;
	}
	
	public long[] getObjectIds() {
		return objectId;
	}

	@Override
	public Packet convertToResponse() {
		objectId = null;
		return this;
	}
	
}
