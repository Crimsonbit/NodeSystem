package at.crimsonbit.nodesystem.events;

import java.util.ArrayList;
import java.util.List;

/**
 * <h1>Stack</h1>
 * <p>
 * The Stack class.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class Stack<T> {

	private List<T> eventStack;
	private int _size;

	public Stack(int size) {
		this._size = size;
		eventStack = new ArrayList<T>();
	}

	public void add(T event) {
		eventStack.add(event);
		checkStackSize();
	}

	public T getLast() {
		return eventStack.get(eventStack.size() - 1);
	}

	public T get(int id) {
		if (id <= this._size) {
			return eventStack.get(id);
		}
		return null;
	}

	private void checkStackSize() {
		if (eventStack.size() > this._size) {
			eventStack.remove(0);
		}
	}
}