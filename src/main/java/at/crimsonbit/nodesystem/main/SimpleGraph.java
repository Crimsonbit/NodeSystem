package at.crimsonbit.nodesystem.main;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem();
		// nodeSystem.getNodeGraph().getGuiMaster().getNodeMaster().registerNodes("at.crimsonbit.nodesystem.node.nodes");
		GNodeGraph graph = nodeSystem.getNodeGraph();

		Scene scene = new Scene(graph, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
