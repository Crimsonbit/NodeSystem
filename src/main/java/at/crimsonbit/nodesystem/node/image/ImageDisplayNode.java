package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageDisplayNode extends AbstractNode {

	@NodeType
	private static final Image type = Image.IMAGE_DISPLAY;

	@NodeField
	@NodeInput
	BufferedImage input;

	@NodeOutput("getInputImage")
	BufferedImage output;

	public ImageDisplayNode() {

	}

	public void getInputImage() {
		if (input != null) {
			output = input;
		}

	}
}
