package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageMultiplyNode extends AbstractNode implements INodeType {

	@NodeType

	@NodeInput
	BufferedImage image_1;

	@NodeInput
	BufferedImage image_2;

	@NodeOutput("computeMultiply")
	BufferedImage output;

	public ImageMultiplyNode() {

	}

	public void computeMultiply() {
		if (image_1 != null && image_2 != null)
			output = ImageUtils.multiply(image_1, image_2);
	}

}
