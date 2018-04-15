package at.crimsonbit.nodesystem.nodebackend.api.dto;

import java.io.Serializable;

public class ConnectionDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7159202030086398297L;
	public final int idOut, idIn;
	public final String fieldOut, fieldIn;

	public ConnectionDTO(int idOut, int idIn, String fieldOut, String fieldIn) {
		super();
		this.idOut = idOut;
		this.idIn = idIn;
		this.fieldOut = fieldOut;
		this.fieldIn = fieldIn;
	}
}
