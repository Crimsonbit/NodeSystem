package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;

public class ImageNegateNode extends AbstractNode implements INodeType {

	@NodeType

	@NodeInput
	BufferedImage image;

	@NodeOutput("computeNegate")
	BufferedImage output;

	public ImageNegateNode() {

	}

	public void computeNegate() {
		if (image != null)
			output = ImageUtils.negate(image);
	}

}
