package at.crimsonbit.nodesystem.node.calculate;

import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class RangeMapNode extends AbstractNode {
	@NodeType
	private static final Calculate type = Calculate.RANGEMAP;

	@NodeInput
	double value;

	@NodeInput
	double min_input;

	@NodeInput
	double max_input;

	@NodeInput
	double min_output;

	@NodeInput
	double max_output;

	@NodeOutput("rangemap")
	double output;

	public RangeMapNode() {

	}

	public void rangemap() {
		double inrange = max_input - min_input;
		/// (0.0))) value = 0.0; // Prevent DivByZero error
		value = (value - min_input) / inrange; // Map input range to [0.0 ... 1.0]
		if (value > max_output) {
			output = max_output;
		}
		if (value < min_output) {
			output = min_output;
		}
		output = (min_output + (max_output - min_output) * value); // Map to output range and return result
	}

}
