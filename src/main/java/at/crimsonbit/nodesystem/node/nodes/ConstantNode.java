package at.crimsonbit.nodesystem.node.nodes;

import at.crimsonbit.nodesystem.node.types.BaseType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ConstantNode extends AbstractNode implements INodeType {

	@NodeType
	private static final BaseType type = BaseType.CONSTANT;

	@NodeField
	@NodeOutput("compute")
	Object constant;

	public void compute() {
	}

}
