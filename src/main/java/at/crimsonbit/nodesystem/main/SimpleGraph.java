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
		GNodeGraph view = nodeSystem.getGUI();

		// graph.getGuiMaster().getNodeMaster().registerNodes("at.crimsonbit.nodesystem.node.nodes");
		// graph.addColorLookup(BaseType.CONSTANT, Color.GREEN);
		// graph.addNodeColorLookup("curve", Color.BLUE);

		Scene scene = new Scene(view, 1024, 768);

		view.addKeySupport();

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		//view.addExternalDEBUGThreads();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
