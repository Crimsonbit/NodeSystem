package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum Terrain implements INodeType {

	DISPLACEMENT("Displacement Node"), DIALTE("Dilate Node"), ERODE("Erode Node");

	private String name;

	private Terrain(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
