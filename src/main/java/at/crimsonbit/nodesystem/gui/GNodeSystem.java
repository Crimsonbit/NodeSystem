package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.Pane;

/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GNodeSystem {

	private GNodeGraph nodeView;

	public GNodeSystem() {
		this.nodeView = new GNodeGraph();
	}

	public GNodeGraph getGUI() {
		return this.nodeView;
	}

}
