package at.crimsonbit.nodesystem.gui.node.port;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * <h1>GPortConHelper</h1>
 * <p>
 * This class is a helper class which is used when two ports are being connected
 * together. It is based on an rectangle which sets a boolean value to true,
 * once the mouse is entered while a drag is being executed. This makes it very
 * easy to determine which port are meant to be connected.
 * </p>
 * 
 * @author Florian Wagner
 *
 */

public class GPortConHelper extends Rectangle {

	private double x = 0;
	private double y = 0;
	private final double WIDTH = 30;
	private final double HEIGHT = 15;
	private final double DRAW_OFFSET = WIDTH / 3d;
	private final double DRAW_OFFSET_OUTPUT = 5d;
	private boolean isInput = false;
	private boolean isMarked = false;
	private boolean doShow = true;

	public GPortConHelper() {
		setX(x);
		setY(y);
		addMouseListeners();
		draw();
	}

	public GPortConHelper(double xs, double ys, boolean input) {
		this.x = xs;
		this.y = ys;
		this.isInput = input;
		setArcHeight(20d);
		setArcWidth(20d);
		if (!isInput)
			setX(xs - DRAW_OFFSET_OUTPUT);
		else
			setX(xs);
		setY(ys);
		if (isInput)
			setTranslateX(-(WIDTH - DRAW_OFFSET));
		addMouseListeners();
		draw();
	}

	private void draw() {
		setFill(Color.TRANSPARENT);
		setWidth(WIDTH);
		setHeight(HEIGHT);
		setY(this.y - HEIGHT / 3);
	}

	public void showHelpers() {
		doShow = true;
	}

	public void hideHelpers() {
		doShow = false;
	}

	private void addMouseListeners() {

		setOnMouseDragEntered(event -> {
			if (doShow) {
				setFill(new Color(0.2d, 0.8d, 0.2d, 0.4d));
			}
			setMarked(true);
		});

		setOnMouseDragExited(event -> {
			setFill(Color.TRANSPARENT);
			setMarked(false);

		});

	}

	public boolean isInput() {
		return isInput;
	}

	public void setInput(boolean isInput) {
		this.isInput = isInput;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

}