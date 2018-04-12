package at.crimsonbit.nodesystem.gui;

import at.crimsonbit.nodesystem.gui.settings.GSettingsPane;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * GNodeView class. This class represents the whole nodegraph. It holds the
 * GNodeGraph.
 * 
 * @author NeonArtworks
 *
 */
public class GNodeView extends AnchorPane {

	private GNodeGraph nodeGraph;
	private GSettingsPane settingsPane;

	public GNodeView() {
		nodeGraph = new GNodeGraph();
		settingsPane = new GSettingsPane(nodeGraph);
		nodeGraph.setSettingsPane(settingsPane);
		getChildren().add(nodeGraph);
		getChildren().add(settingsPane);
		settingsPane.draw();
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

	private void setupScene() {
		setTopAnchor(nodeGraph, 0d);
		setBottomAnchor(nodeGraph, 0d);
		setLeftAnchor(nodeGraph, 0d);
		setRightAnchor(nodeGraph, 0d);

		setBottomAnchor(settingsPane, 0d);
		setRightAnchor(settingsPane, 0d);

	}

}
