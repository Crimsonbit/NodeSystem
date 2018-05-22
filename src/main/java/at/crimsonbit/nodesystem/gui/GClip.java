package at.crimsonbit.nodesystem.gui;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

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
