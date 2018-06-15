package at.crimsonbit.nodesystem.gui;

import java.util.logging.Level;
import java.util.logging.Logger;

import at.crimsonbit.nodesystem.application.ApplicationContext;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;

/**
 * <h1>GraphScene</h1>
 * <p>
 * The GraphScene is the background of the whole graph. It contains the
 * {@link ZoomHandler} aswell as the current x and y positions of the mouse.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class GGraphScene extends AnchorPane implements ILogging {

	private static final double HALF_PIXEL_OFFSET = -0.5;
	private ApplicationContext context = ApplicationContext.getContext();

	private final Canvas canvas = new Canvas();
	private GNodeGraph graph;
	private boolean needsLayout = false;
	private Scale scaleTransform;
	private double scaleValue = 1.0;
	private double strokeValue = 0.1;
	private double lineSpacing = 25;

	private Color lineColor;

	private double localMouseX = getWidth() / 2;
	private double localMouseY = getHeight() / 2;
	private double curX;
	private double curY;
	private double pX;
	private double pY;
	double width;
	double height;
	protected Logger log;

	public GGraphScene() {

		scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);
		// getTransforms().add(scaleTransform);
		getChildren().add(canvas);
		// setStyle("-fx-background-color: #363b3f");

		setOnScroll(new ZoomHandler());
		addEventHandler(MouseEvent.MOUSE_DRAGGED, new MoveHandler());

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

		GClip.install(this);
	}

	public ApplicationContext getApplicationContext() {
		return context;
	}
	
	public Logger getLogger() {
		return log;
	}

	public Scale getScaleTransform() {
		return this.scaleTransform;
	}

	protected void init() {
		setBackground(new Background(new BackgroundFill(
				graph.getGeneralColorLookup().get(GraphSettings.COLOR_BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
		this.lineColor = graph.getGeneralColorLookup().get(GraphSettings.COLOR_BACKGROUND_LINES);
		layoutChildren();
	}

	protected void setNodeGraph(GNodeGraph graph) {
		this.graph = graph;
		init();

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
		if (this.lineColor != null) {
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

				gc.setStroke(this.lineColor);
				gc.setLineWidth(strokeValue);

				final int hLineCount = (int) Math.floor((height + 1) / spacing);
				final int vLineCount = (int) Math.floor((width + 1) / spacing);

				for (int i = 0; i < hLineCount; i++) {
					if (i % 7 == 0) {
						gc.setStroke(Color.BLACK);
						gc.setLineWidth(strokeValue + 2);
					} else {
						gc.setStroke(this.lineColor);
						gc.setLineWidth(strokeValue);
					}
					gc.strokeLine(0, snap((i + 1) * spacing), width, snap((i + 1) * spacing));
				}

				for (int i = 0; i < vLineCount; i++) {
					if (i % 7 == 0) {
						gc.setStroke(Color.BLACK);
						gc.setLineWidth(strokeValue + 2);
					} else {
						gc.setStroke(this.lineColor);
						gc.setLineWidth(strokeValue);
					}
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
		needsLayout = true;
		layoutChildren();

		scaleTransform.setPivotX(localMouseX);
		scaleTransform.setPivotY(localMouseY);
		scaleTransform.setX(scaleValue);
		scaleTransform.setY(scaleValue);

	}

	public void moveTo(double x, double y) {
		needsLayout = true;
		layoutChildren();
		scaleTransform.setPivotX(x);
		scaleTransform.setPivotY(y);

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
	 * <h1>MoveHandler</h1>
	 * <p>
	 * The MoveHandler class is a support class which allows the dragging of the
	 * nodegraph.
	 * </p>
	 * 
	 * @author Florian Wagner
	 *
	 */
	protected class MoveHandler implements EventHandler<MouseEvent> {

		@Override
		// TODO
		public void handle(MouseEvent event) {
			if (event.isMiddleButtonDown()) {
				moveTo(event.getSceneX() / scaleValue, event.getSceneY() / scaleValue);
			}

		}

	}

	/**
	 * <h1>ZoomHandler</h1>
	 * <p>
	 * This handler is responsible for the zooming of the whole node-system.
	 * </p>
	 * 
	 * @author Florian Wagner
	 *
	 */
	protected class ZoomHandler implements EventHandler<ScrollEvent> {
		private static final double MAX_SCALE = 10.0d;
		private static final double MIN_SCALE = 0.4d;

		private static final double DELTA_PLUS = 1.1d;
		private static final double DELTA_MINUS = 0.9d;

		private static final double MIN_SPACING = 10d;
		private static final double MAX_SPACING = 250d;

		private static final double MIN_STROKE = 0.0d;
		private static final double MAX_STROKE = 4d;

		public double clamp(double value, double min, double max) {

			if (Double.compare(value, min) < 0)
				return min;

			if (Double.compare(value, max) > 0)
				return max;

			return value;
		}

		public void handle(ScrollEvent scrollEvent) {

			if (scrollEvent.isControlDown()) {
				if (scrollEvent.getDeltaY() < 0) {
					strokeValue *= DELTA_MINUS;

				} else {
					strokeValue *= DELTA_PLUS;
				}
				strokeValue = clamp(strokeValue, MIN_STROKE, MAX_STROKE);
				if (strokeValue != MIN_STROKE)
					strokeWidth(strokeValue);

			} else {

				if (scrollEvent.getDeltaY() < 0) {
					scaleValue *= DELTA_MINUS;
					lineSpacing *= DELTA_MINUS;
				} else {
					scaleValue *= DELTA_PLUS;
					lineSpacing *= DELTA_PLUS;
				}

				// System.out.println(scaleValue);
				// System.out.println(lineSpacing);
				scaleValue = clamp(scaleValue, MIN_SCALE, MAX_SCALE);
				lineSpacing = clamp(lineSpacing, MIN_SPACING, MAX_SPACING);

				if (scaleValue != MIN_SCALE)
					zoomTo(scaleValue);

			}
			scrollEvent.consume();
		}

	}

	/**
	 * <h1>log({@link Level}, {@link String})</h1>
	 * <p>
	 * By using this method one can log any message to the graph, and to the log
	 * file.
	 * </p>
	 */
	@Override
	public void log(Level l, String msg) {
		if (getLogger() != null)
			log.log(l, msg);
	}

}