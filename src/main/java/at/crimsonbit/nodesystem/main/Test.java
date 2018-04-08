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

@SuppressWarnings({ "restriction", "unused" })
public class Test extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem();
		// nodeSystem.getNodeGraph().getGuiMaster().getNodeMaster().registerNodes("at.crimsonbit.nodesystem.node.nodes");
		GNodeGraph graph = nodeSystem.getNodeGraph();
		GNode constNode1 = new GNode("Constant Node", BaseType.CONSTANT, true, graph);
		GNode constNode2 = new GNode("Constant Node 2", BaseType.CONSTANT, true, graph);
		GNode additionNode1 = new GNode("Addition Node", MathType.ADD, true, graph);
		GNode additionNode2 = new GNode("Addition Node", MathType.ADD, true, graph);
		GNode multiplyNode1 = new GNode("Multiply Node 1", MathType.MULTIPLY, true, graph);
		GNode outputNode = new GNode("Output", BaseType.OUTPUT, true, graph);

		constNode1.getNode().set("constant", 10);
		constNode2.getNode().set("constant", 2);

		graph.getGuiMaster().addNode(constNode1);
		graph.getGuiMaster().addNode(constNode2);
		graph.getGuiMaster().addNode(additionNode1);
		graph.getGuiMaster().addNode(additionNode2);
		graph.getGuiMaster().addNode(multiplyNode1);
		graph.getGuiMaster().addNode(outputNode);

		graph.getGuiMaster().addConnection(constNode1.getOutputPortById(0), additionNode1.getInputPortById(0));
		graph.getGuiMaster().addConnection(constNode2.getOutputPortById(0), additionNode1.getInputPortById(1));
		graph.getGuiMaster().addConnection(constNode2.getOutputPortById(0), multiplyNode1.getInputPortById(0));
		graph.getGuiMaster().addConnection(constNode1.getOutputPortById(0), multiplyNode1.getInputPortById(1));
		graph.getGuiMaster().addConnection(additionNode1.getOutputPorts().get(0), additionNode2.getInputPortById(0));
		graph.getGuiMaster().addConnection(multiplyNode1.getOutputPorts().get(0), additionNode2.getInputPortById(1));

		graph.getGuiMaster().addConnection(additionNode2.getOutputPorts().get(0), outputNode.getInputPortById(0));
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