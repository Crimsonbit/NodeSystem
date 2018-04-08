package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.MathType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class AddNode extends AbstractNode implements INodeType {
	@NodeType
	private static final MathType type = MathType.ADD;

	@NodeInput
	double in_1;

	@NodeInput
	double in_2;

	@NodeOutput("computAdd")
	double output;

	public AddNode() {

	}

	public void computAdd() {
		output = in_1 + in_2;
	}

}
