package at.crimsonbit.nodesystem.gui.dialog;

import javafx.scene.control.SeparatorMenuItem;
/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GSeparator extends SeparatorMenuItem {

	private int id;

	public GSeparator(int id) {
		super();
		setId(String.valueOf(id));
		this.id = id;
		setDisable(true);
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

}
