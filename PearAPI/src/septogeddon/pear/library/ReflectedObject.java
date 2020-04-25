package septogeddon.pear.library;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import septogeddon.pear.api.ObjectReference;

public class ReflectedObject implements Externalizable, ObjectReference {

	private long id;
	private transient Reference<Object> value;
	private transient Object strongReference;
	private transient String serviceName;
	public ReflectedObject() {};
	public ReflectedObject(Object value, long id, String serviceName) {
		if (serviceName == null) {
			this.value = new WeakReference<>(value);
		} else {
			this.strongReference = value;
		}
		this.id = id;
		this.serviceName = serviceName;
	}

	public Field findField(String fieldName)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		return getValue().getClass().getField(fieldName);
	}

	public Method findMethod(String method, Class<?>[] param) throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		return getValue().getClass().getMethod(method, param);
	}

	public long getObjectId() {
		return id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public Object getValue() {
		return strongReference != null ? strongReference : value.get();
	}

	public void setField(String fieldName, Object value)
			throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
		value.getClass().getField(fieldName).set(getValue(), value);
	}

	public boolean shouldFlush() {
		return getValue() == null;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException {
		out.writeLong(id);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		id = in.readLong();
	}

}
