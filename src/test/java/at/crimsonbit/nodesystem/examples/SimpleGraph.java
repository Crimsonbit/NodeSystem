package at.crimsonbit.nodesystem.examples;

import at.crimsonbit.nodesystem.application.NodeSystemBuilder;
import at.crimsonbit.nodesystem.examples.customnode.CustomNodeClassExample;
import at.crimsonbit.nodesystem.examples.customnode.CustomNodes;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.node.image.ImageNodeClass;
import at.crimsonbit.nodesystem.node.types.Image;
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

		GNodeGraph graph = new NodeSystemBuilder(1275, 800, true)
				.registerCustomNodes("at.crimsonbit.nodesystem.examples.customnode")
				.registerCustomNodesJar("Modules/constants.jar").registerColors(Color.SANDYBROWN, CustomNodes.values())
				.registerDefaultNodes(true).build();

		/**
		 * Example of how to add custom node-classes to specific node types
		 */

		/**
		 * Example of how to change settings used in the node-system
		 * 
		 */

		graph.addSetting(GraphSettings.SETTING_CURVE_WIDTH, 6d);
		graph.addSetting(GraphSettings.SETTING_CURVE_CURVE, 100d);
		graph.getGeneralColorLookup().put(GraphSettings.COLOR_BACKGROUND_LINES, Color.WHITE);
		graph.updateColors();

		Scene scene = graph.getNodeScene();
		scene.getStylesheets().add(getClass().getResource("node-menu.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
