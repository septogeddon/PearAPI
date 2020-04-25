package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketFieldSet extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8031729301013930132L;
	private Object value;
	private Long objectId;
	private String info;
	public PacketFieldSet(String info,Object value,long id) {
		this.info = info;
		this.value = value;
		this.objectId = id;
	}
	
	public String getField() {
		return info;
	}
	
	public Object getValue() {
		return value;
	}
	
	public long getObjectId() {
		return objectId;
	}

	@Override
	public Packet convertToResponse() {
		value = null;
		objectId = null;
		info = null;
		return this;
	}
	
}
