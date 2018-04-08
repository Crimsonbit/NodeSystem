package at.crimsonbit.nodesystem.gui.dialog;

import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCodeCombination;
/**
 * 
 * @author NeonArtworks
 *
 */
public class GPopUp extends ContextMenu {

	public GPopUp() {

	}

	public void addItem(GSubMenu menu) {
		getItems().add(menu);
	}

	public void addItem(GEntry item) {
		getItems().add(item);
	}

	public void addItem(int id, String name, KeyCodeCombination kombi) {
		getItems().add(new GEntry(id, name, kombi));
	}

	public void addItem(int id, String name) {
		getItems().add(new GEntry(id, name));

	}

	public void addItem(int id, String name, boolean enabled, KeyCodeCombination kombi) {
		getItems().add(new GEntry(id, name, enabled, kombi));
	}

	public void addItem(int id, String name, boolean enabled) {
		getItems().add(new GEntry(id, name, enabled));

	}

	public void addSeparator(int id) {
		getItems().add(new GSeparator(id));
	}
}
