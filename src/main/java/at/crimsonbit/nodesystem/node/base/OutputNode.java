package at.crimsonbit.nodesystem.node.base;

import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class OutputNode extends AbstractNode {

	@NodeType
	private static final Base type = Base.OUTPUT;

	@NodeInput()
	Object input;

	@NodeOutput("compute")
	Object output;

	public void compute() {
		output = input;
	}

}
