package septogeddon.pear.utils;

public class SneakyThrow {

	@SuppressWarnings("unchecked")
	public static <E extends Throwable> void sneakyThrow(Throwable e) throws E {
	    throw (E) e;
	}
}
