package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

/**
 * <h1>GClip</h1>
 * <p>
 * This class installs a clipping region to a Pane. This can be used if a
 * ScaleTransform is used.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class GClip {
	
	public static void install(Pane region) {
		Rectangle clipRectangle = new Rectangle();
		region.setClip(clipRectangle);
		region.layoutBoundsProperty().addListener((observable, oldValue, newValue) -> {
			clipRectangle.setWidth(newValue.getWidth());
			clipRectangle.setHeight(newValue.getHeight());
		});
	}

}
