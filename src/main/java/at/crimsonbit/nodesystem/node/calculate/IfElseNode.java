package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class IfElseNode extends AbstractNode {
	@NodeType
	private static final Calculate type = Calculate.IF_ELSE;

	@NodeInput
	boolean condition;

	@NodeInput
	Object if_input;

	@NodeInput
	Object else_input;

	@NodeOutput("computeIf")
	Object output;

	public IfElseNode() {

	}

	public void computeIf() {
		if (condition) {
			output = if_input;
		} else {
			output = else_input;
		}

	}

}
