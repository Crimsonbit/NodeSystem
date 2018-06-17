package at.crimsonbit.nodesystem.node.image;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.util.ImageUtils;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageNodeClass extends GNode {
	
	
	public ImageNodeClass() {
		super();
	}

	public ImageNodeClass(String name, int id, boolean draw, GNodeGraph graph) {
		super(name, id, draw, graph);
	}

	public ImageNodeClass(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
	}

	private ImageView drawNodeImage(BufferedImage image, double width) {
		// BufferedImage image = new BufferedImage((int) width, (int) width,
		// BufferedImage.TYPE_INT_ARGB);
		image = ImageUtils.resize(image, (int)width/2, (int)width/2, true);
		//for (int x = 0; x < width; x++)
		//	for (int y = 0; y < width; y++)
		//		image.setRGB(x, y, 0xFF0000);
		Image img = SwingFXUtils.toFXImage(image, null);
		ImageView view = new ImageView(img);
		view.relocate((width/2)-(width/4), 96);
		view.setImage(img);
		return view;

	}

	@Override
	public void draw() {
		if (this.doDraw) {
			if (!toggledDraw) {
				BufferedImage image = (BufferedImage) getAbstractNode().get("output");
				
				double h = height * inPortCount;
				if (inPortCount < outPortcount) {
					h = height * outPortcount;
				}

				double width = 150;
				double tWidth = drawNodeText(name);

				if (width < tWidth)
					width = tWidth;

				h = h + width + 5;

				PORT_OUTPUT_START_X = (int) width;

				drawNodeBase(width, h);
				drawNodeOutline(width, h, active);
				drawNodeTop(width);
				drawNodeShadow();

				addView(text);

				for (GPort p : inputPorts) {
					removeView(p);
					addView(p);
					p.toFront();
				}
				for (GPort p : outputPorts) {
					removeView(p);
					addView(p);
					p.toFront();
				}
				if (image != null) {
					ImageView view = drawNodeImage(image, width);
					addView(view);
				}
				computeUnToggledPortLocations();

			} else {

				double width = 150;
				double tWidth = drawNodeText(name);

				if (width < tWidth)
					width = tWidth;

				drawNodeTopArc(width, 15.0);
				addView(text);
				drawToggledConnections((12.5d/2d)-1d);
			}
		} else {
			getChildren().clear();
		}

	}

}
