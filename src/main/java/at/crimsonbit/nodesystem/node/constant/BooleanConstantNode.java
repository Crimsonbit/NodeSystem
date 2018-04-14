package at.crimsonbit.nodesystem.node.constant;

import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class BooleanConstantNode extends AbstractNode {

	@NodeType
	private static final Constant type = Constant.BOOLEAN;

	@NodeField
	@NodeOutput("compute")
	boolean constant;

	public void compute() {
	}

}
