package at.crimsonbit.nodesystem.main;

import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.gui.GNodeView;
import at.crimsonbit.nodesystem.gui.settings.GSettingsPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem(false);
		GNodeView view = nodeSystem.getGUI();
		GSettingsPane settings = view.getSettingsPane();
		// view.getNodeGraph().getGuiMaster().registerNodes("at.neonartworks.halsdsktp.core.hals.api.nodes");

		view.getNodeGraph().registerNodes("at.crimsonbit.nodesystem.node.nodes");
		
		// view.getNodeGraph().addNodeMenus();
		// view.getNodeGraph().getGuiMaster().getNodeGraph().getGuiMaster().getNodeGraph().getGuiMaster();

		// graph.addColorLookup(BaseType.CONSTANT, Color.GREEN);
		// graph.addNodeColorLookup("curve", Color.BLUE);

		Scene scene = new Scene(view, 1024, 768);
		view.getNodeGraph().addKeySupport();

		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
		// view.addExternalDEBUGThreads();

		/*
		 * Stage s2 = new Stage(); Scene scene2 = new Scene(settings, 1024, 768);
		 * 
		 * scene2.getStylesheets().add(getClass().getResource("application.css").
		 * toExternalForm()); s2.setScene(scene2); s2.show();
		 */
	}

	public static void main(String[] args) {
		launch(args);
	}

}
