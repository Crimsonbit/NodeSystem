package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.Pane;

public class GNodeView extends Pane {

	private GNodeGraph nodeGraph;

	public GNodeView() {
		nodeGraph = new GNodeGraph();
		getChildren().add(nodeGraph);
		getChildren().add(nodeGraph.getNodeInfo());
	}

	public GNodeGraph getNodeGraph() {
		return this.nodeGraph;
	}

}
