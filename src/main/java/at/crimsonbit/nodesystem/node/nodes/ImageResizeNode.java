package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageResizeNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_RESIZE;

	@NodeInput
	BufferedImage image;

	@NodeInput
	int width;

	@NodeInput
	int height;

	@NodeOutput("computeResize")
	BufferedImage output;

	public ImageResizeNode() {

	}

	public void computeResize() {
		if (image != null && width != 0 && height != 0)
			output = ImageUtils.resize(image, width, height, true);
	}

}
