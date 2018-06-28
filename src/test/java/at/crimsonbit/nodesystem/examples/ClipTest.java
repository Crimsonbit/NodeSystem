package at.crimsonbit.nodesystem.examples;

import at.crimsonbit.nodesystem.application.NodeSystemBuilder;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ClipTest extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeGraph graph = new NodeSystemBuilder(1275, 800, true).registerAllModules("Modules/").build();
		graph.setPrefWidth(600);
		graph.setPrefHeight(800);
		Pane root = new Pane();
		Scene mainScene = new Scene(root, 1275, 800);
		mainScene.getStylesheets().add(getClass().getResource("node-menu.css").toExternalForm());
		root.getChildren().add(graph);

		primaryStage.setScene(mainScene);
		primaryStage.show();

	}

	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}
}
