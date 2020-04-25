package septogeddon.pear.api;

import java.io.Serializable;

public abstract class Packet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1111748479146735931L;
	/***
	 * Means that the packet is sent by the server
	 */
	public static final byte MODE_SERVER_TO_CLIENT = 0;
	/***
	 * Means that the packet is sent by the client
	 */
	public static final byte MODE_CLIENT_TO_SERVER = 1;
	private byte mode = MODE_CLIENT_TO_SERVER;
	private long requestId;

	/***
	 * Does nothing at this condition
	 * 
	 * @return the same packet but released some unused resources before sending to
	 *         another peer
	 * @deprecated Should be overridden
	 */
	@Deprecated
	public Packet convertToResponse() {
		return this;
	}

	/***
	 * The packet mode
	 * 
	 * @return mode
	 */
	public byte getMode() {
		return mode;
	}

	/***
	 * The request id in the queue
	 * 
	 * @return the id
	 * @see septogeddon.pear.library.RequestQueue
	 */
	public final long getRequestId() {
		return requestId;
	}

	/***
	 * Handle conversion from Packet {@link MODE_CLIENT_TO_SERVER} to Packet
	 * {@link MODE_SERVER_TO_CLIENT}
	 * 
	 * @return the packet instance
	 */
	public final Packet handleConversion() {
		Packet packet = this.convertToResponse();
		packet.setMode(MODE_SERVER_TO_CLIENT);
		return packet;
	}

	/***
	 * Set the packet mode
	 * 
	 * @param mode
	 * @return the packet instance
	 */
	public Packet setMode(byte mode) {
		this.mode = mode;
		return this;
	}

	/***
	 * Set the request id
	 * 
	 * @param id
	 * @return the packet instance
	 */
	public final Packet setRequestId(long id) {
		requestId = id;
		return this;
	}

}
