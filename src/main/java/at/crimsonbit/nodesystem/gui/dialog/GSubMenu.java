package at.crimsonbit.nodesystem.gui.dialog;

import javafx.scene.control.Menu;
/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GSubMenu extends Menu {
	private int id;
	private String name;

	public GSubMenu(int id, String name) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
	}

	public GSubMenu(int id, String name, boolean enabled) {
		super(name);
		setId(String.valueOf(id));
		this.id = id;
		this.name = name;
		setDisable(enabled);
	}

	public void addMenu(GSubMenu menu) {
		getItems().add(menu);
	}

	public void addItem(GEntry item) {
		getItems().add(item);
	}

	public void addItem(int id, String name) {
		getItems().add(new GEntry(id, name));

	}

	public void addItem(int id, String name, boolean enabled) {
		getItems().add(new GEntry(id, name, enabled));

	}

	public void addSeparator(int id) {
		getItems().add(new GSeparator(id));
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
		return "GSubMenu [id=" + id + ", name=" + name + "]";
	}
}
