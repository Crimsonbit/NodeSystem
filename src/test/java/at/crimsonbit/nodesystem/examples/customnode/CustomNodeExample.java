package at.crimsonbit.nodesystem.examples.customnode;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class CustomNodeExample extends AbstractNode {

	@NodeType
	private static final CustomNodes type = CustomNodes.EXAMPLE;

	@NodeField
	@NodeOutput("compute")
	Object customOutput;

	public void compute() {
		
	}

}
