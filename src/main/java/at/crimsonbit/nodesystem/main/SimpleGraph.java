package at.crimsonbit.nodesystem.main;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.node.types.BaseType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

@SuppressWarnings({ "restriction", "unused" })
public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem();
		// nodeSystem.getNodeGraph().getGuiMaster().getNodeMaster().registerNodes("at.crimsonbit.nodesystem.node.nodes");
		GNodeGraph graph = nodeSystem.getNodeGraph();

		// graph.addColorLookup(BaseType.CONSTANT, Color.GREEN);

		Scene scene = new Scene(graph, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
