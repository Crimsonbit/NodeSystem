package at.crimsonbit.nodesystem.node.image_filter;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageDivideNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_DIVIDE;

	@NodeInput
	BufferedImage image_1;

	@NodeInput
	BufferedImage image_2;

	@NodeOutput("computeDivide")
	BufferedImage output;

	public ImageDivideNode() {

	}

	public void computeDivide() {
		if (image_1 != null && image_2 != null)
			output = ImageUtils.divide(image_1, image_2);
	}

}
