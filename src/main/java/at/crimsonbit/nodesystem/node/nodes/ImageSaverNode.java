package at.crimsonbit.nodesystem.node.nodes;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.crimsonbit.nodesystem.node.types.ImageType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageSaverNode extends AbstractNode implements INodeType {

	@NodeType
	private static final ImageType type = ImageType.IMAGE_SAVER;

	@NodeInput
	BufferedImage image;

	@NodeInput
	String path;

	@NodeOutput("saveImage")
	BufferedImage output;

	public ImageSaverNode() {

	}

	public void genImage() {
		if (image != null && path != null && path != " ") {
			try {
				ImageIO.write(image, ".PNG", new File(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			output = image;
		}
	}

}
