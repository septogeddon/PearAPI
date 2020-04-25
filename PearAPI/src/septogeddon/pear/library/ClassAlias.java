package septogeddon.pear.library;

public class ClassAlias {

	private Class<?> cl;
	private Class<?> a;
	public ClassAlias(Class<?> className,Class<?> alias) {
		this.cl = className;
		this.a = alias;
	}
	
	public Class<?> getOriginal() {
		return cl;
	}
	
	public Class<?> getAlias() {
		return a;
	}
	
}
