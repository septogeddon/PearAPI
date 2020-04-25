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

	@Override
	public Packet convertToResponse() {
		service = null;
		return this;
	}

	public long getObjectId() {
		return object; // will throw NPE if its null and thats ok
	}

	public String getService() {
		return service;
	}

	public void setObjectId(long id) {
		object = id;
	}

}
