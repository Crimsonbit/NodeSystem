package at.crimsonbit.nodesystem.gui;

import at.crimsonbit.nodesystem.util.DragContext;
import at.crimsonbit.nodesystem.util.RangeMapper;
import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GBackground extends Pane {

	// This is to make the stroke be drawn 'on pixel'.
	private static final double HALF_PIXEL_OFFSET = -0.5;

	private final Canvas canvas = new Canvas();
	private boolean needsLayout = false;
	private Scale scaleTransform;
	private double scaleValue = 1.0;
	private double strokeValue = 1.0;
	private double delta = 0.1;

	private double lineSpacing = 25;
	private double r = RangeMapper.mapValue(27, 0, 255, 0, 1);
	private double gr = RangeMapper.mapValue(28, 0, 255, 0, 1);
	private double b = RangeMapper.mapValue(29, 0, 255, 0, 1);

	private double localMouseX = getWidth() / 2;
	private double localMouseY = getHeight() / 2;
	private double lX;
	private double lY;
	private final DragContext dragContext = new DragContext();

	public GBackground() {

		scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
		getTransforms().add(scaleTransform);
		getChildren().add(canvas);
		setStyle("-fx-background-color: #363b3f");
		setOnScroll(new ZoomHandler());

		setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				localMouseX = event.getSceneX();
				localMouseY = event.getSceneY();
			}
		});
		/*
		 * setOnMouseDragged(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { if
		 * (event.isMiddleButtonDown()) { lX = event.getSceneX(); lY =
		 * event.getSceneY();
		 * 
		 * double scale = getScaleValue(); dragContext.x = getBoundsInParent().getMinX()
		 * * scale - event.getScreenX(); dragContext.y = getBoundsInParent().getMinY() *
		 * scale - event.getScreenY(); double offsetX = event.getScreenX() +
		 * dragContext.x; double offsetY = event.getScreenY() + dragContext.y;
		 * 
		 * // adjust the offset in case we are zoomed
		 * 
		 * offsetX /= scale; offsetY /= scale; relocate(offsetX, offsetY);
		 * 
		 * } } });
		 */
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	@Override
	protected void layoutChildren() {

		final int top = (int) snappedTopInset();
		final int right = (int) snappedRightInset();
		final int bottom = (int) snappedBottomInset();
		final int left = (int) snappedLeftInset();
		final int width = (int) getWidth() + left + right;
		final int height = (int) getHeight() + top + bottom;
		final double spacing = lineSpacing;

		canvas.setLayoutX(left);
		canvas.setLayoutY(top);

		if (width != canvas.getWidth() || height != canvas.getHeight() || needsLayout) {
			canvas.setWidth(width);
			canvas.setHeight(height);

			GraphicsContext g = canvas.getGraphicsContext2D();
			g.clearRect(0, 0, width, height);
			g.setStroke(new Color(r, gr, b, 1d));
			g.setLineWidth(strokeValue);
			final int hLineCount = (int) Math.floor((height + 1) / spacing);
			final int vLineCount = (int) Math.floor((width + 1) / spacing);

			for (int i = 0; i < hLineCount; i++) {
				g.strokeLine(0, snap((i + 1) * spacing), width, snap((i + 1) * spacing));
			}

			for (int i = 0; i < vLineCount; i++) {
				g.strokeLine(snap((i + 1) * spacing), 0, snap((i + 1) * spacing), height);
			}
			// getChildren().add(settingsPane);
			needsLayout = false;
		}
	}

	public double getScaleValue() {
		return scaleValue;
	}

	private static double snap(double y) {
		return ((int) y) + HALF_PIXEL_OFFSET;
	}

	public void zoomTo(double scaleValue) {

		this.scaleValue = scaleValue;

		if (lineSpacing > 500)
			lineSpacing = 500;
		if (lineSpacing <= 1)
			lineSpacing = 1;

		needsLayout = true;
		layoutChildren();

		scaleTransform.setX(scaleValue);
		scaleTransform.setPivotX(localMouseX);
		scaleTransform.setPivotY(localMouseY);
		scaleTransform.setY(scaleValue);

	}

	public void strokeWidth(double strokeval) {
		if (strokeval <= 1)
			strokeval = 1;
		if (strokeval > 20)
			strokeval = 20;

		needsLayout = true;
		layoutChildren();
	}

	public class ZoomHandler implements EventHandler<ScrollEvent> {

		public void handle(ScrollEvent scrollEvent) {
			if (!scrollEvent.isControlDown()) {

				if (scrollEvent.getDeltaY() < 0) {
					scaleValue -= delta;
					// lineSpacing -= delta * 5;
				} else {
					scaleValue += delta;
					// lineSpacing += delta * 5;
				}
				if (scaleValue < 1.0) {
					scaleValue = 1.0;
					// lineSpacing = 25;
				}
				zoomTo(scaleValue);

				scrollEvent.consume();
			} else {
				if (scrollEvent.getDeltaY() < 0) {
					strokeValue -= delta;

				} else {
					strokeValue += delta;
				}

				strokeWidth(strokeValue);
				scrollEvent.consume();
			}
		}
	}

}