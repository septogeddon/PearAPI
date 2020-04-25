package septogeddon.pear.api;

public interface Bridge {

	/***
	 * Get the running network
	 * 
	 * @return Network instance
	 */
	public Network getNetwork();

	/***
	 * Send an object (can be a packet) to connected peer
	 * 
	 * @param obj
	 */
	public void send(Packet obj);

	/***
	 * Destroy the bridge including the network ({@link Network#shutdown()})
	 */
	public void shutdown();

	/***
	 * Start the bridge including the network ({@link Network#start()})
	 */
	public void start();

}
