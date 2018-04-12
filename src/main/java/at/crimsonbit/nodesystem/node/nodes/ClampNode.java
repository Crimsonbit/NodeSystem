package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ClampNode extends AbstractNode implements INodeType {
	@NodeType
	private static final Calculate type = Calculate.CLAMP;

	@NodeInput
	double value;

	@NodeInput
	double minVal;

	@NodeInput
	double maxVal;	
	
	
	@NodeOutput("clamp")
	double output;

	public ClampNode() {

	}

	public void clamp() {
		output = Math.min(Math.max(value, minVal), maxVal);
	}

}
