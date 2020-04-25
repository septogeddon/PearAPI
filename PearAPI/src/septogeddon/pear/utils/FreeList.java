package septogeddon.pear.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class FreeList implements List<Object> {

	private final List<Object> list;
	private final boolean safeMode;
	private final boolean compute;

	public FreeList() {
		this(new ArrayList<>());
	}

	public FreeList(List<?> parent) {
		this(parent, false);
	}

	public FreeList(List<?> parent, boolean safe) {
		this(parent, safe, false);
	}

	@SuppressWarnings("unchecked")
	public FreeList(List<?> parent, boolean safe, boolean compute) {
		this.list = (List<Object>) parent;
		this.safeMode = safe;
		this.compute = compute;
	}

	public FreeList(Object parent) {
		this(parent, false, false);
	}

	public FreeList(Object parent, boolean safe) {
		this(parent, safe, false);
	}

	public FreeList(Object parent, boolean safe, boolean compute) {
		this((List<?>) parent, safe, compute);
	}

	@Override
	public void add(int index, Object element) {
		this.list.add(index, element);
	}

	@Override
	public boolean add(Object e) {
		return this.list.add(e);
	}

	@Override
	public boolean addAll(Collection<? extends Object> c) {
		return this.list.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends Object> c) {
		return this.list.addAll(index, c);
	}

	public FreeList build(Object obj) {
		this.add(obj);
		return this;
	}

	@Override
	public void clear() {
		this.list.clear();
	}

	@Override
	public boolean contains(Object o) {
		return this.list.contains(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.list.containsAll(c);
	}

	@Override
	public Object get(int index) {
		return this.list.get(index);
	}

	public byte getB(int index) {
		final Object value = this.get(index);
		return value instanceof Byte ? (Byte) value : this.safeMode ? ((byte) 0) : (Byte) value;
	}

	public Byte getByte(int index) {
		final Object value = this.get(index);
		return value instanceof Byte ? (Byte) value : this.safeMode ? ((byte) 0) : null;
	}

	public char getC(int index) {
		final Object value = this.get(index);
		return value instanceof Character ? (Character) value : this.safeMode ? ((char) 0) : (Character) value;
	}

	public Character getCharacter(int index) {
		final Object value = this.get(index);
		return value instanceof Character ? (Character) value : this.safeMode ? ((char) 0) : null;
	}

	public double getD(int index) {
		final Object value = this.get(index);
		return value instanceof Double ? (Double) value : this.safeMode ? (0) : (Double) value;
	}

	public Double getDouble(int index) {
		final Object value = this.get(index);
		return value instanceof Double ? (Double) value : this.safeMode ? (0D) : null;
	}

	public float getF(int index) {
		final Object value = this.get(index);
		return value instanceof Float ? (Float) value : this.safeMode ? (0) : (Float) value;
	}

	public Float getFloat(int index) {
		final Object value = this.get(index);
		return value instanceof Float ? (Float) value : this.safeMode ? (0f) : null;
	}

	public int getI(int index) {
		final Object value = this.get(index);
		return value instanceof Integer ? (Integer) value : this.safeMode ? (0) : (Integer) value;
	}

	public Integer getInteger(int index) {
		final Object value = this.get(index);
		return value instanceof Integer ? (Integer) value : this.safeMode ? (0) : null;
	}

	public long getL(int index) {
		final Object value = this.get(index);
		return value instanceof Long ? (Long) value : this.safeMode ? (0) : (Long) value;
	}

	public <X> FreeList getList(int index) {
		final Object value = this.get(index);
		return value instanceof FreeList ? (FreeList) value
				: value instanceof List ? new FreeList((List<?>) value, this.safeMode, this.compute)
						: this.safeMode ? new FreeList() : null;
	}

	public Long getLong(int index) {
		final Object value = this.get(index);
		return value instanceof Long ? (Long) value : this.safeMode ? (0L) : null;
	}

	public FreeMap getMap(int index) {
		final Object value = this.get(index);
		return value instanceof FreeMap ? (FreeMap) value
				: value instanceof Map ? new FreeMap((Map<?, ?>) value, this.safeMode, this.compute)
						: this.safeMode ? new FreeMap() : null;
	}

	public short getS(int index) {
		final Object value = this.get(index);
		return value instanceof Short ? (Short) value : this.safeMode ? ((short) 0) : (Short) value;
	}

	public Short getShort(int index) {
		final Object value = this.get(index);
		return value instanceof Short ? (Short) value : this.safeMode ? ((short) 0) : null;
	}

	public String getString(int index) {
		final Object value = this.get(index);
		return value instanceof String ? (String) value
				: this.safeMode ? value == null ? "" : (String.valueOf(value)) : null;
	}

	@Override
	public int indexOf(Object o) {
		return this.list.indexOf(o);
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public Iterator<Object> iterator() {
		return this.list.iterator();
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.list.lastIndexOf(o);
	}

	@Override
	public ListIterator<Object> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public ListIterator<Object> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public Object remove(int index) {
		return this.list.remove(index);
	}

	@Override
	public boolean remove(Object o) {
		return this.list.remove(o);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return this.list.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return this.list.retainAll(c);
	}

	@Override
	public Object set(int index, Object element) {
		return this.list.set(index, element);
	}

	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public FreeList subList(int fromIndex, int toIndex) {
		return new FreeList(this.list.subList(fromIndex, toIndex));
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <X> X[] toArray(X[] a) {
		return this.list.toArray(a);
	}

}
