package at.crimsonbit.nodesystem.node.math;

import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class AddNode extends AbstractNode implements INodeType {

	@NodeType
	private static final Math type = Math.ADD;

	@NodeInput
	double in_1;

	@NodeInput
	double in_2;

	@NodeOutput("computeAdd")
	double output;

	public AddNode() {

	}

	public void computeAdd() {
		output = in_1 + in_2;
	}

}
