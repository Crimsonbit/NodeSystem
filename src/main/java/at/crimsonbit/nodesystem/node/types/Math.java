package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
/**
 * 
 * @author Florian Wagner
 *
 */
public enum Math implements INodeType {
	ADD("Addition Node"), SUBTRACT("Subtraction Node"), MULTIPLY("Multiplier Node"),
	DIVIDE("Divider Node"), MODULO("Modulo Node");

	private String name;

	private Math(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}
	
}
