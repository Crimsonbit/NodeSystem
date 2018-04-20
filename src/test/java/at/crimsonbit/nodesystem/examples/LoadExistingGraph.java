package at.crimsonbit.nodesystem.examples;

import java.net.URISyntaxException;
import java.nio.file.Paths;

import at.crimsonbit.nodesystem.examples.customnode.CustomNodeClassExample;
import at.crimsonbit.nodesystem.examples.customnode.CustomNodes;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.gui.GNodeView;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * This example shows how to load a graph from an existing NodeSystem file.
 * 
 * @author Florian Wagner
 *
 */
public class LoadExistingGraph extends Application {
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

		scene.getStylesheets().add(getClass().getResource("node-menu.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

		/**
		 * Loading an existing graph from file
		 */
		try {
			graph.loadGraphFromFile(Paths.get((getClass().getResource("nodesys.nsys").toURI())));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

}
