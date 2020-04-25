package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketReferenceClosed extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2152166798151396093L;
	private Long objectId;

	public PacketReferenceClosed(long id) {
		objectId = id;
	}

	public Packet convertToResponse() {
		objectId = null;
		return this;
	}

	public long getObjectId() {
		return objectId;
	}

}
