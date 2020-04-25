package septogeddon.pear.api;

public interface Connection {

	/***
	 * Get the client object id represented in the server
	 * @return
	 */
	public long getObjectId();
	/***
	 * Virtually invoke a method in a server from client side
	 * @param name The method name
	 * @param params The parameter type names
	 * @param args The arguments
	 * @param returnType Return type hint
	 * @return Invocation result
	 */
	public Object invokeMethod(String name, String[] params,Object[] args,Class<?> returnType);
	/***
	 * Virtually get a field value in a server from client side
	 * @param info The field name
	 * @param type Field type hint
	 * @return Field value
	 */
	public Object getField(String info,Class<?> type);
	/***
	 * Virtually set a field value in a server from client side
	 * @param info The field name
	 * @param value New value for the field
	 * @param type Field type hint
	 */
	public void setField(String info,Object value,Class<?> type);
	/***
	 * Get the network that hold this object
	 * @return Network instance
	 */
	public Network getNetwork();
	
	/***
	 * Close an object and release resources for both peer
	 * @param obj A connection object
	 */
	public static void close(Object obj) {
		if (obj instanceof ConnectionObject) {
			Connection conn = ((ConnectionObject) obj).ConnectionObject$getConnection();
			conn.getNetwork().closeConnection(conn.getObjectId());
		}
	}
	
}
