package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

/**
 * 
 * @author NeonArtworks
 *
 */
public enum Base implements INodeType {

	CONSTANT("Constant Node"), OUTPUT("Output Node"), PATH("Path Node");

	private String name;

	private Base(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
