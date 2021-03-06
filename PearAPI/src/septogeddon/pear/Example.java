package septogeddon.pear;

import septogeddon.pear.api.Bridge;
import septogeddon.pear.api.Network;
import septogeddon.pear.library.Var;

public class Example {

	public static class Another {
		public String nation = "does";
	}

	public static interface IAnother {
		public Var<String> nation();
	}

	public static interface ITestClass {
		public Var<IAnother> another();

		public Var<String> name();

		public IAnother other();

		public boolean printIt(String string);
	}

	public static class TestClass {
		public Another another = new Another();
		public String name = "yes";

		public Another other() {
			Another a = new Another();
			a.nation = "lol";
			return a;
		}

		public boolean printIt(String string) {
			System.out.println(string);
			return !string.isEmpty();
		}
	}

	public static void main(String[] args) throws Throwable {
		Bridge bridge = new SocketBridge("localhost", 25565, 25565, "this");
		bridge.start();
		Network net = bridge.getNetwork();
		TestClass t = new TestClass();
		net.registerService("serviceExample", t);
		net.start();
		ITestClass test = net.createConnection("serviceExample", ITestClass.class);
		String name = test.name().get();
		System.out.println(name);
		System.out.println(test.printIt(name + ":x"));
		System.out.println(test.another().get().nation().get());
		IAnother another = test.other();
		System.out.println(another);
		System.out.println(another.nation().get());
		System.out.println(test.another().get().nation().get());
		net.shutdown();
	}

}
