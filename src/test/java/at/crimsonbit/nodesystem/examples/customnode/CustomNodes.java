package at.crimsonbit.nodesystem.examples.customnode;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum CustomNodes implements INodeType{

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
