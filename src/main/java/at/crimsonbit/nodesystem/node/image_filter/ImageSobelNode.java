package at.crimsonbit.nodesystem.node.image_filter;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageSobelNode extends AbstractNode {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_SOBEL;

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeSobel")
	BufferedImage output;

	public ImageSobelNode() {

	}

	public void computeSobel() {
		if (image != null)
			output = ImageUtils.Sobel(image);
	}

}
