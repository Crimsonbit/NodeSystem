package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeMaster;
import at.crimsonbit.nodesystem.gui.settings.GGraphSettings;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
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
	private boolean doDisconnect = false;

	private boolean doShow = (boolean) GGraphSettings.getInstance().getSetting(GSettings.SETTING_SHOW_CONNECTION_HELP);;
	private GPort source;

	public GPortConHelper() {
		setX(x);
		setY(y);
		addMouseListeners();
		draw();
	}

	public GPortConHelper(double xs, double ys, boolean input, GPort source) {
		this.x = xs;
		this.y = ys;
		this.isInput = input;
		this.source = source;
		setArcHeight(10d);
		setArcWidth(10d);
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
				GNodeMaster master = this.source.getNode().getNodeGraph().getGuiMaster();
				setFill(new Color(0.8d, 0.2d, 0.2d, 0.4d));

				if (!master.getSourceConPort().isInput()
						&& master.getNodeMaster().checkConnectionPossible(this.source.getNode().getAbstractNode(),
								this.source.getStringID(), master.getSourceConPort().getNode().getAbstractNode(),
								master.getSourceConPort().getStringID())

						|| master.getSourceConPort().isInput() && master.getNodeMaster().checkConnectionPossible(
								master.getSourceConPort().getNode().getAbstractNode(),
								master.getSourceConPort().getStringID(), this.source.getNode().getAbstractNode(),
								this.source.getStringID())) {
					setFill(new Color(0.2d, 0.8d, 0.2d, 0.4d));
				}

				if ((!this.source.isInput() && !master.getSourceConPort().isInput())
						|| (this.source.isInput() && master.getSourceConPort().isInput())
						|| (this.source.getNode().equals(master.getSourceConPort().getNode())))
					setFill(new Color(0.8d, 0.2d, 0.2d, 0.4d));

			}
			setMarked(true);
		});

		setOnMouseDragExited(event -> {
			setFill(Color.TRANSPARENT);
			setMarked(false);

		});

		setOnMouseEntered(event -> {
			if (event.isPrimaryButtonDown()) {
				setFill(new Color(0.8d, 0.2d, 0.2d, 0.4d));
				doDisconnect = true;
			}
		});

		setOnMouseExited(event -> {
			if (event.isPrimaryButtonDown()) {
				if (doDisconnect) {
					setFill(Color.TRANSPARENT);
					GNodeGraph graph = this.source.getNode().getNodeGraph();
					graph.getGuiMaster().removeConnection(this.source);
					graph.update();
					doDisconnect = false;
				}
			}
		});

	}

	public boolean isDoDisconnect() {
		return doDisconnect;
	}

	public void setDoDisconnect(boolean doDisconnect) {
		this.doDisconnect = doDisconnect;
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