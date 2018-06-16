package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum Noise implements INodeType {

	VORONOI("Voronoi-Noise Node"), SIMPLEX("Simplex-Noise Node");

	private String name;

	Noise(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
