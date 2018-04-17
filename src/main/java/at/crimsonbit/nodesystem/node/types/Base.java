package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

/**
 * 
 * @author Florian Wagner
 *
 */
public enum Base implements INodeType {

	OUTPUT("Output Node"), PATH("Path Node");

	private String name;

	private Base(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
