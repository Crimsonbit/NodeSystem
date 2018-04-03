package nodebackend.test;

import at.crimsonbit.nodebackend.api.AbstractNode;
import at.crimsonbit.nodebackend.api.NodeField;
import at.crimsonbit.nodebackend.api.NodeOutput;
import at.crimsonbit.nodebackend.api.NodeType;

public class ConstantNode extends AbstractNode {

	@NodeType
	private static final MyNodeTypes type = MyNodeTypes.CONSTANT;

	@NodeField
	@NodeOutput("compute")
	Object constant;


	public void compute() {

	}

}
