package at.crimsonbit.nodesystem.gui;

import java.util.logging.Level;

import at.crimsonbit.nodesystem.util.logger.SystemLogger;
import javafx.scene.Scene;

public class NodeSystemBuilder {

	private GNodeSystem sys;
	private GNodeView view;
	private GNodeGraph graph;
	private Scene scene;
	private double w;
	private double h;

	public NodeSystemBuilder(double width, double height) {
		this.w = width;
		this.h = height;

	}

	public NodeSystemBuilder init() {
		sys = new GNodeSystem();
		view = sys.getNodeView();
		graph = view.getNodeGraph();
		scene = new Scene(view, w, h);
		graph.setGraphScene(scene);
		return this;
	}

	public NodeSystemBuilder attachSettingsPane() {
		sys.attachSettingsPane(true);
		view = sys.getNodeView();
		graph = view.getNodeGraph();
		scene = new Scene(view, w, h);
		graph.setGraphScene(scene);
		return this;
	}

	public NodeSystemBuilder registerCustomNodes(String pack) {
		if (graph != null)
			graph.registerNodes(pack);

		return this;
	}

	public NodeSystemBuilder attachLogger() {

		SystemLogger.attachLogger();

		return this;
	}

	public NodeSystemBuilder registerDefaultNodes(boolean f) {
		if (graph != null)
			graph.initGraph(f);

		return this;
	}

	public NodeSystemBuilder attachInfo() {
		if (graph != null)
			graph.addInfo();
		return this;
	}

	public GNodeGraph build() {
		this.graph.log(Level.INFO, "NodeSystem ready!");
		return this.graph;
	}

}
