package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

/**
 * 
 * @author NeonArtworks
 *
 */
public enum Calculate implements INodeType {

	ABSOLUTE("Abs. Node"), CLAMP("Clamp Node"), NEGATE("Negate Node"), RANGEMAP("Range-Map node"), 
	BOOL("Bool Node"), EQUAL("Equal Node");
	private String name;
	
	private Calculate(String s) {
		
		this.name = s;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
