package septogeddon.pear.library;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import septogeddon.pear.utils.WeakArrayList;

public class ReflectedObject {

	private long id;
	private List<ReflectedObject> usedIds = new WeakArrayList<>();
	private Reference<Object> value;
	private Object strongReference;
	private String serviceName;

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

	public long getId() {
		return id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public List<ReflectedObject> getUsedIds() {
		return usedIds;
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

}
