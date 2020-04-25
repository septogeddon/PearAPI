package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;
import septogeddon.pear.library.DeliveredThrowable;

public class PacketDeliveredThrowable extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5605293134323891655L;
	private Throwable error;
	public PacketDeliveredThrowable(Throwable t) {
		error = new DeliveredThrowable(t);
	}
	
	public Throwable getThrown() {
		return error;
	}

	@Override
	public Packet convertToResponse() {
		return this;
	}

}
