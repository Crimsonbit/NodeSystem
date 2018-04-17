package at.crimsonbit.nodesystem.examples.customnode;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

/**
 * This example shows how to create custom node types. A node-type is basically
 * an enum which implements {@link INodeType}. It is crucial to implement
 * {@link INodeType}! Other than that it is really not that special. But please
 * keep in mind that the nodesystem is using the toString method internally. It
 * is strongly advised to override the toString method of your custom node
 * types.
 * 
 * @author Florian Wagner
 *
 */
public enum CustomNodes implements INodeType {

	EXAMPLE("Custom Node");

	private String name;

	private CustomNodes(String s) {
		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
