package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.BaseType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class OutputNode extends AbstractNode implements INodeType {

	@NodeType
	private static final BaseType type = BaseType.OUTPUT;

	@NodeInput()
	Object input;

	@NodeOutput("compute")
	Object output;

	public void compute() {
		output = input;
	}

}
