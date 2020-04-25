package septogeddon.pear.api;

public interface Connection {

	public long getObjectId();
	public Object invokeMethod(String name, String[] params,Object[] args,Class<?> returnType);
	public Object getField(String info,Class<?> type);
	public void setField(String info,Class<?> param,Object value);
	public Network getNetwork();
	
	public static void close(Object obj) {
		if (obj instanceof ConnectionObject) {
			Connection conn = ((ConnectionObject) obj).ConnectionObject$getConnection();
			conn.getNetwork().closeConnection(conn.getObjectId());
		}
	}
	
}
