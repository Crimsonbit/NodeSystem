package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.ImageType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageType type = ImageType.IMAGE_EMPTY;

	@NodeInput
	int width;

	@NodeInput
	int height;
	
	@NodeOutput("genImage")
	BufferedImage output;

	public ImageNode() {

	}

	public void genImage() {
		if (width > 0 && height > 0) {
			output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		}
	}

}
