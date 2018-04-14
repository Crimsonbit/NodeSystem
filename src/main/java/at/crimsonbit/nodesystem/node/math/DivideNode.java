package at.crimsonbit.nodesystem.node.math;

import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class DivideNode extends AbstractNode {
	@NodeType
	private static final Math type = Math.DIVIDE;

	@NodeInput
	double in_1;

	@NodeInput
	double in_2;

	@NodeOutput("computMultiply")
	double output;

	public DivideNode() {

	}

	public void computMultiply() {
		output = in_1 / in_2;
	}

}
