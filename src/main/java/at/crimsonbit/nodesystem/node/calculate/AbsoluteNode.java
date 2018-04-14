package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class AbsoluteNode extends AbstractNode {
	@NodeType
	private static final Calculate type = Calculate.ABSOLUTE;

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