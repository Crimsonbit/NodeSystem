package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageCompareSizeNode extends AbstractNode {

	@NodeType
	private static final Image type = Image.IMAGE_COMPARE_SIZE;

	@NodeInput
	BufferedImage image_1;

	@NodeInput
	BufferedImage image_2;

	@NodeOutput("computeSize")
	boolean output;

	public ImageCompareSizeNode() {

	}

	public void computeSize() {
		if (image_1 != null && image_2 != null)
			output = (image_1.getWidth() == image_2.getWidth() && image_1.getHeight() == image_2.getHeight());
	}

}
