package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageNormalizeNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_NORMALIZE;

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeNormalize")
	BufferedImage output;

	public ImageNormalizeNode() {

	}

	public void computeNormalize() {
		if (image != null)
			output = ImageUtils.Normalize(image);
	}

}
