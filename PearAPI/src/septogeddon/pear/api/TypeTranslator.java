package septogeddon.pear.api;

public interface TypeTranslator {

	/***
	 * Convert an object to a safe-to-send object
	 * 
	 * @param obj raw object
	 * @return safe-to-send object
	 */
	public Object unwrap(Object obj);

	/***
	 * Convert a safe-to-send object to a raw object
	 * 
	 * @param obj safe-to-send object
	 * @return raw object
	 */
	public Object wrap(Object obj);

}
