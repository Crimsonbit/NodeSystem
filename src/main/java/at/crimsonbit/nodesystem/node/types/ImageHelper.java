package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum ImageHelper implements INodeType {

	IMAGE_HEIGHT("Image-Height Node"), IMAGE_WIDTH("Image-Width Node"), IMAGE_COMP_SIZE("Image-Compare-Size Node");

	private String name;

	private ImageHelper(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
