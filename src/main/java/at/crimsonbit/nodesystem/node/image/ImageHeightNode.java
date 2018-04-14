package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageHeightNode extends AbstractNode {

	@NodeType
	private static final Image type = Image.IMAGE_HEIGHT;

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeHeight")
	int output;

	public ImageHeightNode() {

	}

	public void computeHeight() {
		if (image != null)
			output = image.getHeight();
	}

}
