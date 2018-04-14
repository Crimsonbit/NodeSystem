package at.crimsonbit.nodesystem.node.math;

import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ModuloNode extends AbstractNode {

	@NodeType
	private static final Math type = Math.MODULO;

	@NodeInput
	double in_1;

	@NodeInput
	double in_2;

	@NodeOutput("computeModulo")
	double output;

	public ModuloNode() {
	}

	public void computeModulo() {
		output = in_1 % in_2;
	}

}
