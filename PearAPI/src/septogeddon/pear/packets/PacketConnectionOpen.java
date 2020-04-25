package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketConnectionOpen extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2473873751070479111L;
	private String className;
	private Long objectId;
	public PacketConnectionOpen(String className,long objectId) {
		this.className = className;
		this.objectId = objectId;
	}
	
	public long getObjectId() {
		return objectId;
	}
	
	public String getClassName() {
		return className;
	}

	@Override
	public Packet convertToResponse() {
//		className = null;
//		objectId = null;
		return this;
	}
	
}
