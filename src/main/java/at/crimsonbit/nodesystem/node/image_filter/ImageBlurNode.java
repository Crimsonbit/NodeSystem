package at.crimsonbit.nodesystem.node.image_filter;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageBlurNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_BLUR;

	@NodeInput
	BufferedImage image;

	@NodeInput
	@NodeField
	int radius;

	@NodeOutput("computeBlur")
	BufferedImage output;

	public ImageBlurNode() {

	}

	public void computeBlur() {
		if (image != null && radius != 0)
			output = ImageUtils.blur2(image, radius);
	}

}
