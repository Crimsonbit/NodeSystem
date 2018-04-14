package at.crimsonbit.nodesystem.node.base;

import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class GroupNode extends AbstractNode {

	@NodeType
	private static final Base type = Base.GROUP;

	@NodeField
	@NodeOutput("compute")
	Object path;

	public void compute() {
	}

}
