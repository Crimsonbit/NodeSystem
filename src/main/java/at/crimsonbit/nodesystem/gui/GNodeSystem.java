package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.Pane;

/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GNodeSystem {

	private GNodeView nodeView;

	public GNodeSystem(boolean addPane) {
		this.nodeView = new GNodeView(addPane);
	}

	public GNodeView getGUI() {
		return this.nodeView;
	}

}
