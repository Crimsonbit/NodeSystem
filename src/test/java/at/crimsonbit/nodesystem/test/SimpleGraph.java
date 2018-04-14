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

		Scene scene = new Scene(view, 1024, 768);
		
		graph.registerNodes("at.crimsonbit.nodesystem.node");
		graph.addInfo();

		/**
		 * These two lines have to be called AFTER the GNodeGraph was added to the
		 * scene!
		 **/

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
