package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.CalculateType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class AbsoluteNode extends AbstractNode implements INodeType {
	@NodeType
	private static final CalculateType type = CalculateType.ABSOLUTE;

	@NodeInput
	double input;

	@NodeOutput("computeAbsolute")
	double output;

	public AbsoluteNode() {

	}

	public void computeAbsolute() {
		if (input < 0)
			input *= -1;
		output = input;
	}

}