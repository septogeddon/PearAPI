package septogeddon.pear.utils;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.WeakHashMap;

public class WeakArrayList<T> extends AbstractList<T> {

	public static void main(String[]args) throws Throwable {
		WeakHashMap<String,String> list = new WeakHashMap<>();
		String nothing = "nothing";
		list.put(nothing,"sa");
		nothing = null;
		for (int i = 0; i < 100; i++) System.gc();
		Thread.sleep(5000);
		System.out.println(list);
	}
	private ArrayList<WeakReference<T>> container = new ArrayList<>();

	@Override
	public T get(int index) {
		return container.get(index).get();
	}

	@Override
	public int size() {
		update();
		return container.size();
	}
	
	public void add(int index,T element) {
		update();
		container.add(index, new WeakReference<T>(element));
	}
	
	private void update() {
		try {
			for (int i = container.size() - 1; i >= 0; i--) {
				WeakReference<T> ref = container.get(i);
				if (ref.get() == null) {
					container.remove(i);
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
