package at.crimsonbit.nodesystem.gui;

import at.crimsonbit.nodesystem.util.GNodeMouseHandler;
import javafx.scene.Group;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GSettingsPane extends Pane {

	@Override
	public void layoutChildren() {
		Group g = new Group();
		Rectangle rect = new Rectangle(350, 500);
		BoxBlur blur = new BoxBlur(3, 3, 3);
		rect.setEffect(blur);
		rect.setFill(new Color(0, 0, 0, 0.3));
		g.getChildren().add(rect);
		getChildren().add(g);
	}

}
