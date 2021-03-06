package at.crimsonbit.nodesystem.nodebackend.testnodes;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class SimpleNode extends AbstractNode {
	@NodeType
	private static final MyNodeTypes type = MyNodeTypes.SIMPLE;

	@NodeInput
	int val1;

	@NodeInput
	String s1;

	@NodeOutput("computeConcat")
	String concat;

	public SimpleNode() {

	}

	public void computeConcat() {
		if (s1 == null) {
			concat = "null";
		} else
			concat = s1 + val1;
	}
}
