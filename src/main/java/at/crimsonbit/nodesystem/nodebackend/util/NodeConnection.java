package at.crimsonbit.nodesystem.nodebackend.util;

import java.lang.reflect.Field;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;

public class NodeConnection {
	private final Field field;
	private final AbstractNode nodeInstance;

	public NodeConnection(Field field, AbstractNode node) {
		this.field = field;
		this.nodeInstance = node;
	}

	public Field getField() {
		return field;
	}

	public AbstractNode getNodeInstance() {
		return nodeInstance;
	}

}
