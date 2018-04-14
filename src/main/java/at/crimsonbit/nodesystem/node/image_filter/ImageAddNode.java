package at.crimsonbit.nodesystem.node.image_filter;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageAddNode extends AbstractNode {

	@NodeType
	private static final ImageFilter type = ImageFilter.IMAGE_ADD;

	@NodeInput
	BufferedImage image_1;

	@NodeInput
	BufferedImage image_2;

	@NodeOutput("computeAdd")
	BufferedImage output;

	public ImageAddNode() {

	}

	public void computeAdd() {
		if (image_1 != null && image_2 != null)
			output = ImageUtils.add(image_1, image_2);
	}

}
