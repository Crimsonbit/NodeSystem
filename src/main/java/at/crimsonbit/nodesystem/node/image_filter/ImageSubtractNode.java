package at.crimsonbit.nodesystem.node.image_filter;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageSubtractNode extends AbstractNode {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_SUBTRACT;

	@NodeInput
	BufferedImage image_1;

	@NodeInput
	BufferedImage image_2;

	@NodeOutput("computeSubtract")
	BufferedImage output;

	public ImageSubtractNode() {

	}

	public void computeSubtract() {
		if (image_1 != null && image_2 != null)
			output = ImageUtils.subtract(image_1, image_2);
	}

}
