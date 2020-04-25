package septogeddon.pear.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FreeMap implements Map<Object, Object> {

	private final Map<Object, Object> map;
	private final boolean safeMode;
	private final boolean compute;

	public FreeMap() {
		this(new HashMap<>());
	}

	public FreeMap(Map<?, ?> parent) {
		this(parent, false);
	}

	public FreeMap(Map<?, ?> parent, boolean safeMode) {
		this(parent, safeMode, false);
	}

	@SuppressWarnings("unchecked")
	public FreeMap(Map<?, ?> parent, boolean safeMode, boolean compute) {
		this.map = (Map<Object, Object>) parent;
		this.safeMode = safeMode;
		this.compute = compute;
	}

	public FreeMap(Object parent) {
		this((Map<?, ?>) parent);
	}

	public FreeMap(Object parent, boolean safeMode) {
		this((Map<?, ?>) parent, safeMode);
	}

	public FreeMap(Object parent, boolean safeMode, boolean compute) {
		this((Map<?, ?>) parent, safeMode, compute);
	}

	public FreeMap build(Object key, Object value) {
		this.put(key, value);
		return this;
	}

	@Override
	public void clear() {
		this.map.clear();
	}

	private <X> X compute(Object key, X x) {
		if (this.compute)
			try {
				this.put(key, x);
			} catch (final ClassCastException e) {
			}
		return x;
	}

	@Override
	public boolean containsKey(Object key) {
		return this.map.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return this.map.containsValue(value);
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		return this.map.entrySet();
	}

	@Override
	public Object get(Object key) {
		return this.map.get(key);
	}

	public byte getB(Object key) {
		final Object value = this.get(key);
		return value instanceof Byte ? (Byte) value : this.safeMode ? this.compute(key, (byte) 0) : (Byte) value;
	}

	public Byte getByte(Object key) {
		final Object value = this.get(key);
		return value instanceof Byte ? (Byte) value : this.safeMode ? this.compute(key, (byte) 0) : null;
	}

	public char getC(Object key) {
		final Object value = this.get(key);
		return value instanceof Character ? (Character) value
				: this.safeMode ? this.compute(key, (char) 0) : (Character) value;
	}

	public Character getCharacter(Object key) {
		final Object value = this.get(key);
		return value instanceof Character ? (Character) value : this.safeMode ? this.compute(key, (char) 0) : null;
	}

	public double getD(Object key) {
		final Object value = this.get(key);
		return value instanceof Double ? (Double) value : this.safeMode ? this.compute(key, 0) : (Double) value;
	}

	public Double getDouble(Object key) {
		final Object value = this.get(key);
		return value instanceof Double ? (Double) value : this.safeMode ? this.compute(key, 0D) : null;
	}

	public float getF(Object key) {
		final Object value = this.get(key);
		return value instanceof Float ? (Float) value : this.safeMode ? this.compute(key, 0) : (Float) value;
	}

	public Float getFloat(Object key) {
		final Object value = this.get(key);
		return value instanceof Float ? (Float) value : this.safeMode ? this.compute(key, 0f) : null;
	}

	public int getI(Object key) {
		final Object value = this.get(key);
		return value instanceof Integer ? (Integer) value : this.safeMode ? this.compute(key, 0) : (Integer) value;
	}

	public Integer getInteger(Object key) {
		final Object value = this.get(key);
		return value instanceof Integer ? (Integer) value : this.safeMode ? this.compute(key, 0) : null;
	}

	public long getL(Object key) {
		final Object value = this.get(key);
		return value instanceof Long ? (Long) value : this.safeMode ? this.compute(key, 0) : (Long) value;
	}

	public FreeList getList(Object key) {
		final Object value = this.get(key);
		return value instanceof FreeList ? (FreeList) value
				: value instanceof List ? new FreeList((List<?>) value, this.safeMode, this.compute)
						: this.safeMode ? new FreeList() : null;
	}

	public Long getLong(Object key) {
		final Object value = this.get(key);
		return value instanceof Long ? (Long) value : this.safeMode ? this.compute(key, 0L) : null;
	}

	public <K1, V1> FreeMap getMap(Object key) {
		final Object value = this.get(key);
		return value instanceof FreeMap ? (FreeMap) value
				: value instanceof Map ? new FreeMap((Map<?, ?>) value, this.safeMode, this.compute)
						: this.safeMode ? new FreeMap() : null;
	}

	public short getS(Object key) {
		final Object value = this.get(key);
		return value instanceof Short ? (Short) value : this.safeMode ? this.compute(key, (short) 0) : (Short) value;
	}

	public Short getShort(Object key) {
		final Object value = this.get(key);
		return value instanceof Short ? (Short) value : this.safeMode ? this.compute(key, (short) 0) : null;
	}

	public String getString(Object key) {
		final Object value = this.get(key);
		return value instanceof String ? (String) value
				: this.safeMode ? value == null ? "" : this.compute(key, String.valueOf(value)) : null;
	}

	@Override
	public boolean isEmpty() {
		return this.map.isEmpty();
	}

	public boolean isSafe() {
		return this.safeMode;
	}

	@Override
	public Set<Object> keySet() {
		return this.map.keySet();
	}

	@Override
	public Object put(Object key, Object value) {
		return this.map.put(key, value);
	}

	@Override
	public void putAll(Map<? extends Object, ? extends Object> m) {
		this.map.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		return this.map.remove(key);
	}

	public boolean shouldCompute() {
		return this.compute;
	}

	@Override
	public int size() {
		return this.map.size();
	}

	@Override
	public Collection<Object> values() {
		return this.map.values();
	}

}
