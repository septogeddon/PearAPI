package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketFieldGet extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1089741456096401639L;
	private long objectId;
	private String info;
	private Object value;
	private String hint;
	public PacketFieldGet(long id,String info,Class<?> type) {
		this.objectId = id;
		this.info = info;
		this.hint = type.getName();
	}
	
	public String getHintClassName() {
		return hint;
	}
	
	public String getField() {
		return info;
	}
	
	public long getObjectId() {
		return objectId;
	}
	
	public void setValue(Object val) {
		value = val;
	}
	
	public Object getValue() {
		return value;
	}
	
	@Override
	public Packet convertToResponse() {
		info = null;
		return this;
	}
	
}
