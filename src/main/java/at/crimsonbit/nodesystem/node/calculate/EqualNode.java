package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class EqualNode extends AbstractNode {
	@NodeType
	private static final Calculate type = Calculate.EQUAL;

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
