package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketRequestService extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6182259970915560480L;
	private String service;
	private Long object;
	
	public PacketRequestService(String service) {
		this.service = service;
	}
	
	public void setObjectId(long id) {
		object = id;
	}
	
	public String getService() {
		return service;
	}
	
	public long getObjectId() {
		return object; // will throw NPE if its null and thats ok
	}
	@Override
	public Packet convertToResponse() {
		service = null;
		return this;
	}
	
}
