package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.Pane;

public class GNodeSystem {

	private GNodeGraph graph;

	public GNodeSystem() {
		this.graph = new GNodeGraph();
	}

	public GNodeGraph getNodeGraph() {
		return this.graph;
	}

}
