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

	public GNodeSystem() {
		this.nodeView = new GNodeView();
	}

	public GNodeView getGUI() {
		return this.nodeView;
	}

}
