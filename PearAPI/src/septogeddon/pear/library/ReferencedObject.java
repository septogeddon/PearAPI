package septogeddon.pear.library;

import java.io.Serializable;

public class ReferencedObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6521765995695605878L;

	private long objectId;
	public ReferencedObject(long objectId) {
		this.objectId = objectId;
	}
	
	public long getObjectId() {
		return objectId;
	}
	
}
