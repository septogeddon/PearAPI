package septogeddon.pear.library;

import java.lang.reflect.Type;

import septogeddon.pear.api.Connection;

public class Var<T> {

	private final Class<?> declaring;
	private final Connection connection;
	private final String name;

	public Var(Connection con, String n, Type decl) {
		name = n;
		connection = con;
		declaring = (Class<?>) decl;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		return (T) connection.getField(name, declaring);
	}

	public T set(T t) {
		connection.setField(name, t, declaring);
		return t;
	}

	public String toString() {
		return "field(" + name + "=" + declaring + ")";
	}
}
