package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageWidthNode extends AbstractNode {

	@NodeType
	private static final Image type = Image.IMAGE_WIDTH;

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeWidth")
	int output;

	public ImageWidthNode() {

	}

	public void computeWidth() {
		if (image != null)
			output = image.getWidth();
	}

}
