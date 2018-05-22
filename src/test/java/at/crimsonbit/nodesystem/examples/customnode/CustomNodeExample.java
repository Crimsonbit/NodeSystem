package at.crimsonbit.nodesystem.examples.customnode;

import java.lang.annotation.Annotation;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

/**
 * <h1>Example</h1>
 * <p>
 * This class shows, how to add logic to your custom node. In this case, we set
 * the output of the node to the received input, if it is not null. It is always
 * strongly advised to make null-checks and not sane-value checks.
 * </p>
 * <h2>Node Type</h2>
 * <p>
 * Every not MUST extend {@link AbstractNode}. To define a node-type you have to
 * make a static final {@link INodeType} 'type' and use the {@link Annotation}
 * {@link NodeType}. This can be any type you want, but keep in mind that the
 * nodesystem will always use the last registered node if multiple nodes have
 * the same type!
 * </p>
 * <h2>Node Inputs</h2>
 * <p>
 * To define inputs you have to just create a variable (the name can be chosen
 * as you wish). In order that the nodesystem can use the variable you have to
 * make an {@link NodeInput} {@link Annotation}.
 * </p>
 * <h3>Node Fields</h3>
 * <p>
 * A node input can also have a {@link NodeField} annotation. By using
 * {@link NodeField} you can also set the value of the variable outside of the
 * node.
 * </p>
 * <h2>Node Outputs</h2>
 * <p>
 * To make a node-output you have to create a variable (the name can be chosen
 * to your liking) which has the {@link Annotation} {@link NodeOutput}. The
 * {@link NodeOutput} annotation want's a string as input. The string to put
 * here is the method that should be used to compute the output. This way you
 * can make multiple outputs that use different compute-methods in one node.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class CustomNodeExample extends AbstractNode {

	@NodeType
	private static final CustomNodes type = CustomNodes.EXAMPLE;
	
	@NodeField
	@NodeInput
	Object input;

	
	@NodeOutput("compute")
	Object outpu;

	public void compute() {
		if (input != null)
			outpu = input;
	}

}
