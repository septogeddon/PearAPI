package septogeddon.pear;

import septogeddon.pear.api.Bridge;
import septogeddon.pear.api.Connection;
import septogeddon.pear.api.Network;
import septogeddon.pear.library.Var;

public class Example {

	public static void main(String[]args) throws Throwable {
		Bridge bridge = new SocketBridge("localhost", 25565, 25565,"this");
		bridge.start();
		Network net = bridge.getNetwork();
		net.registerService("serviceExample", new TestClass());
		net.start();
		ITestClass test = net.createConnection("serviceExample", ITestClass.class);
		String name = test.name().get();
		System.out.println(name);
		System.out.println(test.printIt(name+":x"));
		System.out.println(test.another().get().nation().get());
		IAnother another = test.other();
		System.out.println(test.other().nation().get());
		Connection.close(another);
		System.out.println(another.nation().get());
		net.shutdown();
	}
	
	public static interface ITestClass {
		public Var<String> name();
		public Var<IAnother> another();
		public boolean printIt(String string);
		public IAnother other();
	}
	public static interface IAnother {
		public Var<String> nation();
	}
	
	public static class Another {
		public String nation = "does";
	}
	
	public static class TestClass {
		public Another another = new Another();
		public String name = "yes";
		public boolean printIt(String string) {
			System.out.println(string);
			return !string.isEmpty();
		}
		public Another other() {
			Another a = new Another();
			a.nation = "lol";
			return a;
		}
	}
	
}
