package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeField;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageLoaderNode extends AbstractNode implements INodeType {

	@NodeType
	private static final Image type = Image.IMAGE_LOADER;

	@NodeField
	@NodeInput
	String path;

	@NodeOutput("genImage")
	BufferedImage output;

	public ImageLoaderNode() {

	}

	public void genImage() {
		if (path != null && path != " " && path != "")
			try {
				output = ImageIO.read(new File(path));
			} catch (IOException e) {
				output = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
			}

	}

}
