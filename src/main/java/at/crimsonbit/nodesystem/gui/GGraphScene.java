package at.crimsonbit.nodesystem.gui;

import at.crimsonbit.nodesystem.util.RangeMapper;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 * 
 * @author NeonArtworks
 *
 */
public class GGraphScene extends AnchorPane {

	private static final double HALF_PIXEL_OFFSET = -0.5;

	private final Canvas canvas = new Canvas();
	private GNodeGraph graph;
	private boolean needsLayout = false;
	private Scale scaleTransform;
	private double scaleValue = 1.0;
	private double strokeValue = 0.2;
	private double delta = 0.1;

	private double lineSpacing = 25;
	private double r = RangeMapper.mapValue(27, 0, 255, 0, 1);
	private double g = RangeMapper.mapValue(28, 0, 255, 0, 1);
	private double b = RangeMapper.mapValue(29, 0, 255, 0, 1);
	private Color lineColor;

	private double localMouseX = getWidth() / 2;
	private double localMouseY = getHeight() / 2;
	private double curX;
	private double curY;
	private double pX;
	private double pY;
	double width;
	double height;

	public GGraphScene() {

		scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
		getTransforms().add(scaleTransform);
		getChildren().add(canvas);
		// setStyle("-fx-background-color: #363b3f");

		setOnScroll(new ZoomHandler());

		setTopAnchor(canvas, 0d);
		setBottomAnchor(canvas, 0d);
		setLeftAnchor(canvas, 0d);
		setRightAnchor(canvas, 0d);

		setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				localMouseX = event.getSceneX();
				localMouseY = event.getSceneY();
				pX = event.getScreenX();
				pY = event.getScreenY();
				curX = event.getSceneX();
				curY = event.getSceneY();
			}
		});

		setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (graph.getActive() != null) {
					graph.getActive().setActive(false);
					graph.getActive().redraw();
					graph.setActive(null);
				}
			}
		});
	}
	
	private void init() {
		setBackground(new Background(
				new BackgroundFill(graph.getGeneralColorLookup().get("background"), CornerRadii.EMPTY, Insets.EMPTY)));
		this.lineColor = graph.getGeneralColorLookup().get("line_color");
	}

	protected void setNodeGraph(GNodeGraph graph) {
		this.graph = graph;
		init();
		layoutChildren();
	}

	public double getLocalMouseX() {
		return localMouseX;
	}

	public void setLocalMouseX(double localMouseX) {
		this.localMouseX = localMouseX;
	}

	public double getLocalMouseY() {
		return localMouseY;
	}

	public void setLocalMouseY(double localMouseY) {
		this.localMouseY = localMouseY;
	}

	public double getCurX() {
		return curX;
	}

	public void setCurX(double curX) {
		this.curX = curX;
	}

	public double getCurY() {
		return curY;
	}

	public void setCurY(double curY) {
		this.curY = curY;
	}

	public double getpX() {
		return pX;
	}

	public void setpX(double pX) {
		this.pX = pX;
	}

	public double getpY() {
		return pY;
	}

	public void setpY(double pY) {
		this.pY = pY;
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	@Override
	protected void layoutChildren() {
		if (lineColor != null) {
			final int top = (int) snappedTopInset();
			final int right = (int) snappedRightInset();
			final int bottom = (int) snappedBottomInset();
			final int left = (int) snappedLeftInset();
			width = (int) (getWidth() + left + right);
			height = (int) (getHeight() + top + bottom);
			final double spacing = lineSpacing;

			canvas.setLayoutX(left);
			canvas.setLayoutY(top);

			if (width != canvas.getWidth() || height != canvas.getHeight() || needsLayout) {
				canvas.setWidth(width);
				canvas.setHeight(height);

				GraphicsContext gc = canvas.getGraphicsContext2D();
				gc.clearRect(0, 0, width, height);

				gc.setStroke(new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 1d));
				gc.setLineWidth(strokeValue);

				final int hLineCount = (int) Math.floor((height + 1) / spacing);
				final int vLineCount = (int) Math.floor((width + 1) / spacing);

				for (int i = 0; i < hLineCount; i++) {
					gc.strokeLine(0, snap((i + 1) * spacing), width, snap((i + 1) * spacing));
				}

				for (int i = 0; i < vLineCount; i++) {
					gc.strokeLine(snap((i + 1) * spacing), 0, snap((i + 1) * spacing), height);
				}
				// getChildren().add(settingsPane);
				needsLayout = false;
			}
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

		scaleTransform.setPivotX(localMouseX);
		scaleTransform.setPivotY(localMouseY);
		scaleTransform.setX(scaleValue);
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

	/**
	 * 
	 * @author NeonArtworks
	 *
	 */
	protected class ZoomHandler implements EventHandler<ScrollEvent> {
		private static final double MAX_SCALE = 10.0d;
		private static final double MIN_SCALE = 1d;

		public double clamp(double value, double min, double max) {

			if (Double.compare(value, min) < 0)
				return min;

			if (Double.compare(value, max) > 0)
				return max;

			return value;
		}

		public void handle(ScrollEvent scrollEvent) {

			if (!scrollEvent.isControlDown()) {

				if (scrollEvent.getDeltaY() < 0) {
					scaleValue -= delta;

				} else {
					scaleValue += delta;

				}
				scaleValue = clamp(scaleValue, MIN_SCALE, MAX_SCALE);

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