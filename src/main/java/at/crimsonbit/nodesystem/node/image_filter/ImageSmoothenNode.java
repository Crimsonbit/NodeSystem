package at.crimsonbit.nodesystem.node.image_filter;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageSmoothenNode extends AbstractNode {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_SMOOTHEN;

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeSmooth")
	BufferedImage output;

	public ImageSmoothenNode() {

	}

	public void computeSmooth() {
		if (image != null)
			output = ImageUtils.Smoothen(image);
	}

}
