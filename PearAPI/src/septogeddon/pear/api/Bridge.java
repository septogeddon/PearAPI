package septogeddon.pear.api;

public interface Bridge {

	/***
	 * Get the running network
	 * @return Network instance
	 */
	public Network getNetwork();
	/***
	 * Send an object (can be a packet) to connected peer
	 * @param obj
	 */
	public void send(Object obj);
	/***
	 * Start the bridge including the network ({@link Network#start()})
	 */
	public void start();
	/***
	 * Destroy the bridge including the network ({@link Network#shutdown()})
	 */
	public void shutdown();
	
}
