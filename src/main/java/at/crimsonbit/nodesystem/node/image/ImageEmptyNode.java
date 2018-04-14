package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageEmptyNode extends AbstractNode {

	@NodeType
	private static final Image type = Image.IMAGE_EMPTY;

	@NodeInput
	int width;

	@NodeInput
	int height;

	@NodeOutput("genImage")
	BufferedImage output;

	public ImageEmptyNode() {

	}

	public void genImage() {
		if (width > 0 && height > 0) {
			output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
	}

}
