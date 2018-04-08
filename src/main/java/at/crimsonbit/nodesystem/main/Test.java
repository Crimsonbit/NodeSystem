package at.crimsonbit.nodesystem.main;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.GNodeConnection;
import at.crimsonbit.nodesystem.node.types.BaseType;
import at.crimsonbit.nodesystem.node.types.MathType;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem();
		// nodeSystem.getNodeGraph().getGuiMaster().getNodeMaster().registerNodes("at.crimsonbit.nodesystem.node.nodes");

		GNodeGraph graph = nodeSystem.getNodeGraph();
		GNode node1 = new GNode("Const", BaseType.CONSTANT, true, graph);
		GNode node2 = new GNode("Add", MathType.ADD, true, graph);
		GNode node3 = new GNode("Output", BaseType.OUTPUT, true, graph);

		graph.getGuiMaster().addNode(node1);
		graph.getGuiMaster().addNode(node2);
		graph.getGuiMaster().addNode(node3);
		
		graph.getGuiMaster().addConnection(node1.getOutputPortById(0), node2.getInputPortById(0));
		// System.out.println();
		graph.getGuiMaster().addConnection(node2.getOutputPorts().get(0), node3.getInputPortById(0));

		graph.update();
		Scene scene = new Scene(graph, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}