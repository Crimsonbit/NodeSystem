package at.crimsonbit.nodesystem.examples;

import java.io.IOException;
import java.util.logging.Logger;

import at.crimsonbit.nodesystem.examples.customnode.CustomNodeClassExample;
import at.crimsonbit.nodesystem.examples.customnode.CustomNodes;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.gui.GNodeView;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.util.logger.SystemLogger;
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
		
		GNodeSystem nodeSystem = new GNodeSystem(false);
		GNodeView view = nodeSystem.getNodeView();
		GNodeGraph graph = view.getNodeGraph();

		Scene scene = new Scene(view, 1024, 768);

		graph.registerNodes("at.crimsonbit.nodesystem.examples.customnode"); // has to be called before initGraph!
		graph.addInfo(); // Adds information to the top of the screen

		/**
		 * initGraph() has to be called AFTER the graph was added to the scene!
		 **/

		graph.initGraph(true);

		/**
		 * Example of how to add custom node-classes to specific node types
		 */
		graph.addCustomNode(CustomNodes.EXAMPLE, new CustomNodeClassExample().getClass());
		graph.addColorLookup(CustomNodes.EXAMPLE, Color.SANDYBROWN); // Sets the color of our custom node.

		/**
		 * Example of how to change settings used in the node-system
		 * 
		 */
		graph.addSetting(GraphSettings.SETTING_CURVE_WIDTH, 6d);
		graph.addSetting(GraphSettings.SETTING_CURVE_CURVE, 100d);
		graph.getGeneralColorLookup().put(GraphSettings.COLOR_BACKGROUND_LINES, Color.WHITE);
		graph.updateColors();

		scene.getStylesheets().add(getClass().getResource("node-menu.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
