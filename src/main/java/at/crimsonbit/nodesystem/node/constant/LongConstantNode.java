package at.crimsonbit.nodesystem.node.constant;

import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class LongConstantNode extends AbstractNode implements INodeType {

	@NodeType
	private static final Constant type = Constant.LONG;

	@NodeField
	@NodeOutput("compute")
	long constant;

	public void compute() {
	}

}
