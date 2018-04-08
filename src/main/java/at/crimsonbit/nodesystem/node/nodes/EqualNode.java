package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.CalculateType;
import at.crimsonbit.nodesystem.node.types.MathType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class EqualNode extends AbstractNode implements INodeType {
	@NodeType
	private static final CalculateType type = CalculateType.EQUAL;

	@NodeInput
	Object in_1;

	@NodeInput
	Object in_2;

	@NodeOutput("computeEqual")
	boolean output;

	public EqualNode() {

	}

	public void computeEqual() {
		output = (in_1 == in_2);
	}

}
