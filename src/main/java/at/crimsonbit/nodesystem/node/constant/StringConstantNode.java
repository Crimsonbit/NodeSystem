package at.crimsonbit.nodesystem.node.constant;

import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class StringConstantNode extends AbstractNode {

	@NodeType
	private static final Constant type = Constant.STRING;

	@NodeField
	@NodeOutput("compute")
	String constant;

	public void compute() {
	}

}
