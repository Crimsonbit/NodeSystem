package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;

public class ImageSaverNode extends AbstractNode {

	@NodeType
	private static final Image type = Image.IMAGE_SAVER;
	private boolean save = true;
	
	@NodeInput
	BufferedImage image;

	@NodeInput
	String path;

	@NodeOutput("saveImage")
	BufferedImage output;

	public ImageSaverNode() {

	}

	public void saveImage() {
		if (image != null && path != null && path != " " && save) {
			try {
				ImageIO.write(image, "PNG", new File(path));
				// System.out.println(image);
				// System.out.println(path);
				// System.out.println("Saving image....");
				save = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			output = image;
		}
	}

}
