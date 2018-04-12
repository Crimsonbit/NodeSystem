package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class SubtractNode extends AbstractNode implements INodeType {
	@NodeType
	private static final Math type = Math.SUBTRACT;

	@NodeInput
	double in_1;

	@NodeInput
	double in_2;

	@NodeOutput("computeSubtract")
	double output;

	public SubtractNode() {

	}

	public void computeSubtract() {
		output = in_1 - in_2;
	}

}
