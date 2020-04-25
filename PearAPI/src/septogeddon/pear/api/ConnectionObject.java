package septogeddon.pear.api;

import java.lang.reflect.Method;

public interface ConnectionObject {

	public static final Method GETTER_METHOD = ConnectionObject.class.getMethods()[0];
	
	/***
	 * An (invisible?) method that is used to get the Connection of this object
	 * @return Connection instance that handle this object
	 */
	public Connection ConnectionObject$getConnection();
	
}
