package at.crimsonbit.nodesystem.util;

import java.util.HashMap;
import java.util.Map;

/**
 * <h1>BaseContainer</h1> <br>
 * <p>
 * The BaseContainer can store <b> any </b> value to a corresponding id. This
 * makes it a lot easier to store variables inside a program, because instead of
 * a lot variables you have one container. This makes it very easy to clean your
 * code.
 * </p>
 * 
 * 
 * @author Florian Wagner
 * @version 0.1
 */
public class BaseContainer {

	private Map<String, Object> container; // main container

	/**
	 * <h1>public BaseContainer({@link Integer} entries)</h1> <br>
	 * <p>
	 * Creates a new instance of the BaseContainer. The value entries is used to
	 * set the maximum amount of entries.<br>
	 * If entries equals zero, no maximum will be set!
	 * </p>
	 * 
	 * @param entries
	 *            The maximum amount of entries in this container.
	 */
	public BaseContainer(int entries) {
		if (entries >= 0) {
			if (entries == 0)
				container = new HashMap<String, Object>();
		} else
			container = new HashMap<String, Object>(entries);

	}

	/**
	 * <h1>public void addEntrie({@link String} id, {@link Object} num)</h1>
	 * <p>
	 * Adds a new entry to the BaseContainer. <br>
	 * The entry value 'num' will be stored with the corresponding id set with
	 * the first parameter 'id'
	 * </p>
	 * 
	 * @param id
	 *            The id you want to store a value to.
	 * @param num
	 *            The Object/Value you want to store to the id.
	 */
	public void addEntry(String id, Object num) {
		container.put(id, num);
	}

	/**
	 * <h1>public {@link Object} getEntryById({@link String} id)</h1>
	 * <p>
	 * Returns the corresponding value or Object of the id. <br>
	 * If the the id was not found a default value of -1 will be returned!
	 * </p>
	 * 
	 * @param id
	 *            The id you want to get.
	 * @return The value, or if not found, -1.
	 */
	public Object getEntryById(String id) {
		return container.getOrDefault(id, -1);
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Object> getBaseContainer() {
		return this.container;
	}

	/**
	 * 
	 * @param con
	 */
	public void setBaseContainer(Map<String, Object> con) {
		this.container = con;
	}

	/**
	 * 
	 * @return
	 */
	public int getContainerLength() {
		return container.size();
	}

	/**
	 * 
	 * @param num
	 * @return
	 */

	public Object GetEntryByNum(int num) {
		int tmpC = 0;
		for (Object o : container.entrySet()) {
			if (tmpC == num) {
				return o;
			}
			tmpC++;
		}
		return null;
	}

	public void PrintEntries() {
		for (Object o : container.entrySet()) {
			System.out.println(o);
		}
	}
}