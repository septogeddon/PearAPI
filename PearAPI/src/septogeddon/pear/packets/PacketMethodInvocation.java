package septogeddon.pear.packets;

import septogeddon.pear.api.Packet;

public class PacketMethodInvocation extends Packet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1078047382767083334L;
	private long objectId;
	private Object[] args;
	private String info;
	private String[] pars;
	private Object value;
	private String hintClass;
	public PacketMethodInvocation(long id,String info,String[] pars,Object[] args,Class<?> hint) {
		this.info = info;
		this.pars = pars;
		this.objectId = id;
		this.args = args;
		this.hintClass = hint.getName();
	}
	
	public String getHintClassName() {
		return hintClass;
	}
	
	public void setValue(Object val) {
		value = val;
	}
	
	public Object getValue() {
		return value;
	}
	
	public long getObjectId() {
		return objectId;
	}
	
	
	public Object[] getArguments() {
		return args;
	}
	
	public String getMethod() {
		return info;
	}
	
	public String[] getParameters() {
		return pars;
	}

	@Override
	public Packet convertToResponse() {
		info = null;
		args = null;
		return this;
	}
	
}
