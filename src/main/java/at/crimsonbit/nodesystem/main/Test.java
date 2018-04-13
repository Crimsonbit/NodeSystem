package at.crimsonbit.nodesystem.main;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeSystem;
import at.crimsonbit.nodesystem.gui.GNodeView;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.node.types.Math;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application {

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Node Editor");

		GNodeSystem nodeSystem = new GNodeSystem(false);
		// nodeSystem.getNodeGraph().getGuiMaster().getNodeMaster().registerNodes("at.crimsonbit.nodesystem.node.nodes");
		GNodeView view = nodeSystem.getGUI();
		GNodeGraph graph = view.getNodeGraph();
		
		graph.registerNodes("at.crimsonbit.nodesystem.node.base");
		graph.registerNodes("at.crimsonbit.nodesystem.node.image");
		graph.registerNodes("at.crimsonbit.nodesystem.node.math");
		graph.registerNodes("at.crimsonbit.nodesystem.node.calculate");
		graph.registerNodes("at.crimsonbit.nodesystem.node.image_filter");
		graph.loadMenus();
		
		GNode constNode1 = new GNode("Constant Node", Base.CONSTANT, true, graph);
		GNode constNode2 = new GNode("Constant Node 2", Base.CONSTANT, true, graph);
		GNode additionNode1 = new GNode("Addition Node", Math.ADD, true, graph);
		GNode additionNode2 = new GNode("Addition Node", Math.ADD, true, graph);
		GNode multiplyNode1 = new GNode("Multiply Node 1", Math.MULTIPLY, true, graph);
		GNode outputNode = new GNode("Output", Base.OUTPUT, true, graph);

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

		Scene scene = new Scene(view, 1024, 768);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}