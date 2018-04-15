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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((field == null) ? 0 : field.hashCode());
		result = prime * result + ((nodeInstance == null) ? 0 : nodeInstance.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeConnection other = (NodeConnection) obj;
		if (field == null) {
			if (other.field != null)
				return false;
		} else if (!field.equals(other.field))
			return false;
		if (nodeInstance == null) {
			if (other.nodeInstance != null)
				return false;
		} else if (!nodeInstance.equals(other.nodeInstance))
			return false;
		return true;
	}

}
