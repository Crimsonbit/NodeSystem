package at.crimsonbit.nodesystem.node.constant;

import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class IntegerConstantNode extends AbstractNode {

	@NodeType
	private static final Constant type = Constant.INTEGER;

	@NodeField
	@NodeOutput("compute")
	int constant;

	public void compute() {
	}

}
