package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.AnchorPane;

/**
 * 
 * GNodeView class. This class represents the whole nodegraph. It holds the
 * GNodeGraph.
 * 
 * @author Florian Wagner
 *
 */
public class GNodeView extends AnchorPane {

	private GNodeGraph nodeGraph;
	//private GSettingsPane settingsPane;
	//private boolean addSettingsPane = true;

	public GNodeView(boolean addSettingsPane) {
		//this.addSettingsPane = addSettingsPane;
		nodeGraph = new GNodeGraph();
		//settingsPane = new GSettingsPane(nodeGraph);
		//nodeGraph.setSettingsPane(settingsPane);
		getChildren().add(nodeGraph);
		//if (addSettingsPane) {
			//getChildren().add(settingsPane);
			//settingsPane.draw();
		//}
		// getChildren().add(nodeGraph.getNodeInfo());
		setupScene();
	}

	/**
	 * Returns the nodeGraph.
	 * 
	 * @return the GNodeGraph.
	 */
	public GNodeGraph getNodeGraph() {
		return this.nodeGraph;
	}

	//public GSettingsPane getSettingsPane() {
	//	return this.settingsPane;
	//}

	private void setupScene() {
		setTopAnchor(nodeGraph, 0d);
		setBottomAnchor(nodeGraph, 0d);
		setLeftAnchor(nodeGraph, 0d);
		setRightAnchor(nodeGraph, 0d);
		//if (this.addSettingsPane) {
		//	setBottomAnchor(settingsPane, 0d);
		//	setRightAnchor(settingsPane, 0d);
		//}

	}

}
