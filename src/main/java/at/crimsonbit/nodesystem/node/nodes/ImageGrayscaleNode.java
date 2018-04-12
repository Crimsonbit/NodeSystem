package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageGrayscaleNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_GRAYSCALE;

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeGrayscale")
	BufferedImage output;

	public ImageGrayscaleNode() {

	}

	public void computeGrayscale() {
		if (image != null)
			output = ImageUtils.CGS(image);
	}

}
