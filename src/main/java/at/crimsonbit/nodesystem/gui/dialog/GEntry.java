package at.crimsonbit.nodesystem.gui.dialog;

import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCodeCombination;
/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GEntry extends MenuItem {

	private int id;
	private String name;

	public GEntry(int id, String name) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
	}

	public GEntry(int id, String name, boolean enabled) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
		setDisable(enabled);
	}

	public GEntry(int id, String name, KeyCodeCombination kombi) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
		setAccelerator(kombi);
	}

	public GEntry(int id, String name, boolean enabled, KeyCodeCombination kombi) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
		setDisable(enabled);
		setAccelerator(kombi);
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "GEntry [id=" + id + ", name=" + name + "]";
	}

}
