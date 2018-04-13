package at.crimsonbit.nodesystem.node.base;

import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ConstantNode extends AbstractNode implements INodeType {

	@NodeType
	private static final Base type = Base.CONSTANT;
	
	@NodeField
	@NodeOutput("compute")
	Object constant;

	public void compute() {
	}

}
