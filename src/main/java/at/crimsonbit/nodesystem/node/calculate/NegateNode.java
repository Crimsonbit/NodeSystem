package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class NegateNode extends AbstractNode implements INodeType {
	@NodeType
	private static final Calculate type = Calculate.NEGATE;

	@NodeInput
	double input;

	@NodeOutput("negate")
	double output;

	public NegateNode() {

	}

	public void negate() {
		output = (input * -1);
	}

}
