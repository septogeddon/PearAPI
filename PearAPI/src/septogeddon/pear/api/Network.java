package septogeddon.pear.api;

import septogeddon.pear.library.ReflectedObject;

public interface Network extends AutoCloseable {

	/***
	 * Start this network manually
	 * @see Bridge#start()
	 */
	public void start();
	/***
	 * Shutdown this network manually
	 * @see Bridge#shutdown()
	 */
	public void shutdown();
	/***
	 * Get an object from another peer
	 * @param <T> The object generic type
	 * @param serviceId The service name registered on another peer
	 * @param interf An interface that virtually representing the object
	 * @return A connection object covered with generic type (instance of {@link ConnectionObject})
	 */
	public <T> T createConnection(String serviceId,Class<T> interf);
	/***
	 * Get an existing object based by represented object id
	 * @param objectId The object id
	 * @return a connection that already made before
	 */
	public ConnectionObject getExistingConnection(long objectId);
	/***
	 * Register a service that can be fetched later in another peer
	 * @param serviceId The service name
	 * @param obj The service object
	 * @return An object that handles reflection for the service object
	 */
	public ReflectedObject registerService(String serviceId,Object obj);
	/***
	 * Register a class alias for another peer that doesn't have the declared class
	 * @param cl Original class
	 * @param interf Interface class to represent the original class in another peer
	 */
	public void registerInterface(Class<?> cl,Class<?> interf);
	/***
	 * Unregister a class alias from this network
	 * @param cl Original class
	 */
	public void unregisterInterface(Class<?> cl);
	/***
	 * Register a {@link TypeTranslator}
	 * @param tr The translator service
	 * @param cl Classes accepted for this translator service
	 */
	public void registerTypeTranslator(TypeTranslator tr,Class<?>... cl);
	/***
	 * Send a packet to another peer
	 * @param <T> Packet generic type
	 * @param packet The packet
	 * @return The same packet but this contains response
	 */
	public <T extends Packet> T sendPacket(T packet);
	/***
	 * Unwrap an object that previously translated using {@link TypeTranslator}
	 * @param obj The object
	 * @return Unwrapped object
	 */
	public Object unwrap(Object obj);
	/***
	 * Wrap an object to a read-to-send object using {@link TypeTranslator}
	 * @param value The object
	 * @param hint The object type name hint
	 * @return
	 */
	public Object wrap(Object value,String hint);
	/***
	 * Dispatch a packet manually
	 * @param packet The packet instance
	 */
	public void dispatchPacket(Packet packet);
	/***
	 * Close all connections for both peer
	 */
	public void close();
	/***
	 * Close specific connection
	 * @param id The object id
	 */
	public void closeConnection(long id);
	
}
