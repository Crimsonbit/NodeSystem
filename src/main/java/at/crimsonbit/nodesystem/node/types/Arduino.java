package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum Arduino implements INodeType {

	DEVICE("Arduino-Device Node"), PORT("Arduino-Port Node"), CLOSER("Arduino-Device-Closer Node"), LISTENER(
			"Arduino-Listener Node");
	private String name;

	Arduino(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
