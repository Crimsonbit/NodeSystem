package at.crimsonbit.nodesystem.nodebackend.api.dto;

import java.io.Serializable;

public class FieldDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4073023480184784688L;
	public final String name;
	public final Object value;

	public FieldDTO(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}
}
