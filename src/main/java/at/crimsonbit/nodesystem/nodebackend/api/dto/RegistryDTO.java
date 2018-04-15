package at.crimsonbit.nodesystem.nodebackend.api.dto;

import java.io.Serializable;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public class RegistryDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7217406229463327414L;

	public final INodeType type;

	public final Class<? extends AbstractNode> clazz;

	public RegistryDTO(INodeType type, Class<? extends AbstractNode> clazz) {
		super();
		this.type = type;
		this.clazz = clazz;
	}
}
