package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class IfNode extends AbstractNode implements INodeType {
	@NodeType
	private static final Calculate type = Calculate.IF;

	@NodeInput
	boolean if_;

	@NodeInput
	Object object;

	@NodeOutput("computeIf")
	Object output;

	public IfNode() {

	}

	public void computeIf() {
		if (if_)
			output = object;
	}

}
