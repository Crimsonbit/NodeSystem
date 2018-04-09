package nodebackend.test.constnode;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import nodebackend.test.MyNodeTypes;

public class ConstantNode extends AbstractNode {

	@NodeType
	private static final MyNodeTypes type = MyNodeTypes.CONSTANT;

	@NodeField
	@NodeOutput("compute")
	Object constant;

	public void compute() {

	}

}
