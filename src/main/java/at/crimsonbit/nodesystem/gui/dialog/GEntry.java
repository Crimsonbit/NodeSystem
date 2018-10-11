package at.crimsonbit.nodesystem.gui.dialog;

import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCodeCombination;

/**
 * 
 * @author Florian Wagner
 *
 */
public class GEntry extends MenuItem {

	private int id;
	private String name;
	private String unlocname = "";

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

	public GEntry(int id, String name, String unlocname, boolean enabled) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
		this.unlocname = unlocname;
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

	public String getUnlocalizedName() {
		return unlocname;
	}

	public void setUnlocalizedName(String unlocname) {
		this.unlocname = unlocname;
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
		setText(name);
		this.name = name;
	}

	@Override
	public String toString() {
		return "GEntry [id=" + id + ", name=" + name + "]";
	}

}
