package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public enum Image implements INodeType {
	IMAGE_EMPTY("Empty Image Node"), IMAGE_LOADER("Image-Loader Node"), IMAGE_SAVER("Image-Saver Node"), IMAGE_WIDTH(
			"Image-width Node"), IMAGE_HEIGHT("Image-height Node"), IMAGE_COMPARE_SIZE("Image-Size-Compare Node");

	private String name;

	private Image(String s) {

		this.name = s;
	}

	@Override
	public String toString() {
		return this.name;
	}
}
