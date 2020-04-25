package septogeddon.pear.api;

public interface Bridge {

	public Network getNetwork();
	public void send(Object obj);
	public void start();
	public void shutdown();
	
}
