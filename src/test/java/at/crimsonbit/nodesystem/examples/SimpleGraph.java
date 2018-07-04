package at.crimsonbit.nodesystem.examples;

import at.crimsonbit.nodesystem.application.NodeGraphBuilder;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.color.GColors;
import at.crimsonbit.nodesystem.gui.color.GTheme;
import at.crimsonbit.nodesystem.gui.settings.GGraphSettings;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This example shows how to create a new nodegraph, add a custom node and a
 * custom node class to the nodesystem.
 * 
 * 
 * 
 * @author Florian Wagner
 *
 */
public class SimpleGraph extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeGraph graph = new NodeGraphBuilder(1275, 800, true).registerAllModules("Modules/").build();

		/**
		 * Example of how to change settings used in the node-system
		 * 
		 */

		GGraphSettings.getInstance().set(GSettings.SETTING_CURVE_WIDTH, 6d);
		GGraphSettings.getInstance().set(GSettings.SETTING_CURVE_CURVE, 100d);
		GTheme.getInstance().putColor(GColors.COLOR_BACKGROUND_LINES, Color.WHITE);
		graph.updateColors();

		Scene scene = graph.getNodeScene();
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	/**
	 * This is only needed for arduino nodes. The Serial connection works on a
	 * different thread than the JavaFX Main thread.
	 */
	@Override
	public void stop() throws Exception {
		super.stop();
		System.exit(0);
	}

	public static void main(String[] args) {
		launch(args);
	}

}
