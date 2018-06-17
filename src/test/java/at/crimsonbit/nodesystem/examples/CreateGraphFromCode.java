package at.crimsonbit.nodesystem.examples;

import at.crimsonbit.nodesystem.application.NodeSystemBuilder;
import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.node.base.OutputNodeClass;
import at.crimsonbit.nodesystem.node.constant.ConstantNodeClass;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.node.types.Math;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * This example shows how to create nodes and connections aswell as setting node
 * values completely trhough code.
 * 
 * @author Florian Wagner
 *
 */
public class CreateGraphFromCode extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeGraph graph = new NodeSystemBuilder(1275, 800, true)
				.registerCustomNodes("at.crimsonbit.nodesystem.examples.customnode").registerDefaultNodes(true)
				.attachInfo().build();

		GNode constNode1 = new ConstantNodeClass("Constant Node", Constant.DOUBLE, true, graph, 70, 250);
		GNode constNode2 = new ConstantNodeClass("Constant Node 2", Constant.INTEGER, true, graph, 70, 350);
		GNode additionNode1 = new GNode("Addition Node", Math.ADD, true, graph, 300, 200);
		GNode additionNode2 = new GNode("Addition Node", Math.ADD, true, graph, 500, 300);
		GNode multiplyNode1 = new GNode("Multiply Node 1", Math.MULTIPLY, true, graph, 300, 400);
		GNode outputNode = new OutputNodeClass("Output", Base.OUTPUT, true, graph, 700, 300);

		constNode1.getAbstractNode().set("constant", 1337.12345);
		constNode2.getAbstractNode().set("constant", 2);

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

		Scene scene = graph.getNodeScene();
		scene.getStylesheets().add(getClass().getResource("node-menu.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}