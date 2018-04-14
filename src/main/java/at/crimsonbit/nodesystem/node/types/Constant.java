package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum Constant implements INodeType {

	STRING("String-Constant Node"), INTEGER("Integer-Constant Node"), DOUBLE("Double-Constant Node"), FLOAT(
			"Float-Constant Node"), LONG("Long-Constant Node"), CHAR("Char-Constant Node"), BOOLEAN(
					"Boolean-Constant Node"), SHORT("Short-Constant Node"), BYTE("Byte-Constant Node");

	private String name;

	private Constant(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}

}
