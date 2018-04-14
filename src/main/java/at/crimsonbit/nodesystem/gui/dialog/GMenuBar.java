package at.crimsonbit.nodesystem.gui.dialog;

import javafx.scene.control.MenuBar;

public class GMenuBar extends MenuBar {

	public GMenuBar() {

	}

	public void addMenu(GSubMenu menu) {
		getMenus().add(menu);
	}

}
