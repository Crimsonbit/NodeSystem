package at.crimsonbit.nodesystem.test;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.gui.GNodeView;
import at.crimsonbit.nodesystem.gui.node.GGroupNode;
import at.crimsonbit.nodesystem.node.types.Base;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem(false);
		GNodeView view = nodeSystem.getGUI();
		GNodeGraph graph = view.getNodeGraph();

		graph.registerNodes("at.crimsonbit.nodesystem.node");
		// graph.registerNodes("at.crimsonbit.nodesystem.node.image");
		// graph.registerNodes("at.crimsonbit.nodesystem.node.math");
		// graph.registerNodes("at.crimsonbit.nodesystem.node.calculate");
		// graph.registerNodes("at.crimsonbit.nodesystem.node.image_filter");
		// graph.registerNodes("at.crimsonbit.nodesystem.node.constant");

		graph.addInfo();
		Scene scene = new Scene(view, 1024, 768);

		graph.initGraph();
		graph.addCustomNode(Base.GROUP, new GGroupNode().getClass());
		
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
