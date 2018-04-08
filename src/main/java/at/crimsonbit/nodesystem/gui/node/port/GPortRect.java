package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.node.GNode;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
/**
 * 
 * @author NeonArtworks
 *
 */
public class GPortRect extends Rectangle {
	
	
	private GNode node;
	public GPortRect(double x, double y, boolean input, GNode node) {
		this.node = node;
		setX(x);
		setY(y);
		setWidth(6);
		setHeight(6);
		if (input)
			setFill(Color.BLUE);
		else
			setFill(Color.RED);
		setStroke(Color.LIGHTSKYBLUE);
		setArcWidth(20.0);
		setArcHeight(20.0);
		setStrokeWidth(1);

		

	}
}
