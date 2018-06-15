package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class IfObject extends AbstractNode {
	@NodeType
	private static final Calculate type = Calculate.IF_OBJECT;

	@NodeInput
	Object obj_condition;

	@NodeInput
	Object object;

	@NodeOutput("computeIf")
	Object output;

	public IfObject() {

	}

	public void computeIf() {
		if (obj_condition == object)
			output = object;
	}

}
