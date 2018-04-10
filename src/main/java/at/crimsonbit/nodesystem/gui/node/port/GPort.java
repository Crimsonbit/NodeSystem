package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurve;

/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GPort extends Group implements IGConsumable {

	private double x;
	private double y;
	private int id;
	private boolean input;
	private GNode node;
	private CubicCurve line = new CubicCurve();
	private GPopUp dialog;
	private String stringID;
	private GPort thisPort;
	private GPortLabel label;
	private GPortRect rect;
	private long currentTime;
	private long lastTime;

	private long currentRemoveTime;
	private long lastRemoveTime;

	private void addPopUpHandler(GPopUp dialog) {
		addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				long diff = 0;
				currentTime = System.currentTimeMillis();
				dialog.hide();
				if (lastTime != 0 && currentTime != 0) {
					diff = currentTime - lastTime;

					if (diff <= 215) {

						// System.out.println("double");
						GNodeGraph graph = node.getNodeGraph();
						// graph.getGuiMaster().addConnection(node1, node2);
						if (isInput())
							graph.getGuiMaster().setSecondPort(thisPort);
						else
							graph.getGuiMaster().setFirstPort(thisPort);
						graph.getGuiMaster().connectPorts();
						graph.update();
					}

				}

				lastTime = currentTime;
			}

		});

		this.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {

			node.setPortPressed(false);
		});
		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			if (event.isSecondaryButtonDown()) {
				long diff = 0;
				currentRemoveTime = System.currentTimeMillis();
				dialog.hide();
				if (lastRemoveTime != 0 && currentRemoveTime != 0) {
					diff = currentRemoveTime - lastRemoveTime;

					if (diff <= 700) {
						GNodeGraph graph = node.getNodeGraph();
						graph.getGuiMaster().removeConnection(this);
						graph.update();
					}
				}
				lastRemoveTime = currentRemoveTime;
			}
		});

		/*
		 * for (MenuItem item : this.dialog.getItems()) { int id =
		 * Integer.valueOf(item.getId()); item.setOnAction(event -> { consumeMessage(id,
		 * (GEntry) item); event.consume(); }); }
		 * 
		 */
		// addSubeMenuHandlers();

		/*
		 * for (MenuItem item : this.popUpDialog.getItems()) { int id =
		 * Integer.valueOf(item.getId()); item.setOnAction(event -> { //
		 * System.out.println(id); consumeMessage(id); event.consume(); });
		 * 
		 * }
		 */
	}

	public void draw() {
		getChildren().add(label);
		getChildren().add(rect);
	}

	public void redraw() {
		getChildren().clear();
		getChildren().add(label);
		getChildren().add(rect);
	}

	public GPortRect getPortRectangle() {
		return this.rect;
	}

	public GPort(int id, boolean input, String labels, double x, double y, GNode node) {
		this.node = node;

		this.id = id;
		this.stringID = labels;
		this.input = input;
		this.x = x;
		this.y = y;
		label = new GPortLabel(x, y, labels, input);
		rect = new GPortRect(x, y, input, node);

		GPopUp pop = new GPopUp();
		pop.addItem(-1, labels, true);
		pop.addItem(0, "Connect");
		pop.addItem(1, "Disconnect");
		this.dialog = pop;

		addPopUpHandler(pop);
		draw();
		this.thisPort = this;
		// getChildren().add(line);
		/*
		 * setOnMouseEntered(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { node.setPortPressed(true);
		 * 
		 * } });
		 * 
		 * setOnMouseReleased(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { node.setPortPressed(false);
		 * 
		 * } });
		 * 
		 * setOnMouseDragExited(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { node.setPortPressed(false);
		 * 
		 * } });
		 * 
		 * setOnMouseExited(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { node.setPortPressed(false);
		 * 
		 * } }); ;
		 * 
		 * setOnMouseDragged(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { // System.out.println("hi");
		 * node.setPortPressed(true); line.setStartX(x); line.setStartY(y);
		 * 
		 * if (event.isPrimaryButtonDown()) {
		 * line.endXProperty().bind(node.layoutXProperty().add(event.getSceneX()));
		 * line.endYProperty().bind(node.layoutYProperty().add(event.getSceneY())); }
		 * 
		 * } });
		 */

	}

	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public void setPopUpDialog(GPopUp pop) {
		this.dialog = pop;
		addPopUpHandler(dialog);
	}

	public GPopUp getPopUpDialog() {
		return this.dialog;
	}

	public GNode getNode() {
		return this.node;
	}

	public int getID() {
		return this.id;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public boolean isInput() {
		return input;
	}

	public void setInput(boolean input) {
		this.input = input;
	}

	@Override
	public void consumeMessage(int id, GEntry source) {
		if (id == 0) {
			GNodeGraph graph = node.getNodeGraph();
			// graph.getGuiMaster().addConnection(node1, node2);
			if (isInput())
				graph.getGuiMaster().setSecondPort(this);
			else
				graph.getGuiMaster().setFirstPort(this);
			graph.getGuiMaster().connectPorts();
			graph.update();
		} else if (id == 1) {
			GNodeGraph graph = node.getNodeGraph();
			graph.getGuiMaster().removeConnection(this);
			graph.update();
		}
	}

}
