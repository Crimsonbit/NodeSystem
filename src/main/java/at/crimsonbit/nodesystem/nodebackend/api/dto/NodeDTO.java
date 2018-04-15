package at.crimsonbit.nodesystem.nodebackend.api.dto;

import java.io.Serializable;

import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;

public class NodeDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7479025808995373595L;
	public final Class<? extends AbstractNode> clazz;
	public final FieldDTO[] fields;
	public final int id;

	public NodeDTO(Class<? extends AbstractNode> clazz, int id, FieldDTO... fields) {
		super();
		this.clazz = clazz;
		if (fields == null) {
			fields = new FieldDTO[0];
		}
		this.fields = fields;
		this.id = id;
	}
}
