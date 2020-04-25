package septogeddon.pear.api;

import java.io.Serializable;

public abstract class Packet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1111748479146735931L;
	public static final byte MODE_SERVER_TO_CLIENT = 0;
	public static final byte MODE_CLIENT_TO_SERVER = 1;
	public static final byte MODE_ANONYMOUS_TO_ANONYMOUS = 2;
	private byte mode = MODE_CLIENT_TO_SERVER;
	private long requestId;
	
	@Deprecated
	public Packet convertToResponse() {
		return this;
	}
	
	public final long getRequestId() {
		return requestId;
	}
	
	public byte getMode() {
		return mode;
	}
	
	public Packet setMode(byte mode) {
		this.mode = mode;
		return this;
	}
	
	public final Packet setRequestId(long id) {
		requestId = id;
		return this;
	}
	
	public final Packet handleConversion() {
		Packet packet = this.convertToResponse();
		packet.setMode(MODE_SERVER_TO_CLIENT);
		return packet;
	}
	
}
