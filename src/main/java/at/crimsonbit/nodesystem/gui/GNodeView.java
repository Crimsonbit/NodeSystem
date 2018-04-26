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
	// private GSettingsPane settingsPane;
	// private boolean addSettingsPane = true;
	private GLogPane logPane;
	
	public GNodeView(boolean addSettingsPane) {
		// this.addSettingsPane = addSettingsPane;
		logPane = new GLogPane();
		nodeGraph = new GNodeGraph(logPane);

		// settingsPane = new GSettingsPane(nodeGraph);
		// nodeGraph.setSettingsPane(settingsPane);

		getChildren().add(nodeGraph);

		// logPane.getChildren().add(rect);
		getChildren().add(logPane);

		// if (addSettingsPane) {
		// getChildren().add(settingsPane);
		// settingsPane.draw();
		// }
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

	// public GSettingsPane getSettingsPane() {
	// return this.settingsPane;
	// }

	private void setupScene() {
		setTopAnchor(nodeGraph, 0d);
		setBottomAnchor(nodeGraph, 0d);
		setLeftAnchor(nodeGraph, 0d);
		setRightAnchor(nodeGraph, 0d);

		setTopAnchor(logPane, 0d);
		setRightAnchor(logPane, 0d);
		setBottomAnchor(logPane, 700d);
		// if (this.addSettingsPane) {
		// setBottomAnchor(settingsPane, 0d);
		// setRightAnchor(settingsPane, 0d);
		// }

	}

}
