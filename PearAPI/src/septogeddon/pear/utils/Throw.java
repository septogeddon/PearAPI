package septogeddon.pear.utils;

public class Throw {

	@SuppressWarnings("unchecked")
	public static <E extends Throwable> void throwable(Throwable e) throws E {
		throw (E) e;
	}
}
