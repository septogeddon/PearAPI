package septogeddon.pear.api;

import java.lang.reflect.Method;

public interface ConnectionObject {

	public static final Method GETTER_METHOD = ConnectionObject.class.getMethods()[0];
	public Connection ConnectionObject$getConnection();
	
}
