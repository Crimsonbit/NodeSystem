package at.crimsonbit.nodesystem.node.constant;

import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class DoubleConstantNode extends AbstractNode implements INodeType {

	@NodeType
	private static final Constant type = Constant.DOUBLE;
	
	@NodeField
	@NodeOutput("compute")
	double constant;

	public void compute() {
	}


}
