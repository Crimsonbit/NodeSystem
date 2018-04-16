package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author NeonArtworks
 *
 */

public class GPort extends Group implements IGConsumable {

	private double x;
	private double y;
	private int id;
	private boolean input;
	private GNode node;

	private GPopUp dialog;
	private String stringID;
	private GPortLabel label;
	private GPortRect rect;

	private final Tooltip tooltip = new Tooltip();

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

		// mouseHandler();
		draw();

		for (MenuItem item : this.dialog.getItems()) {
			int idd = Integer.valueOf(item.getId());
			item.setOnAction(event -> { //
				// System.out.println(idd);
				consumeMessage(idd, (GEntry) event.getSource());
				event.consume();
			});
		}
		this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
			dialog.show(this, event.getScreenX(), event.getScreenY());
			event.consume();
		});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			dialog.hide();
		});

		tooltip.setText("GPort: " + this.id + "\n" + "Input: " + this.input + "\n" + "Type: "
				+ node.getAbstractNode().get(labels));
		Tooltip.install(this, tooltip);

		// getChildren().add(line);
		/*
		 * setOnMouseEntered(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) {
		 * node.setPortPressed(true);
		 * 
		 * } });
		 * 
		 * setOnMouseReleased(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) {
		 * node.setPortPressed(false);
		 * 
		 * } });
		 * 
		 * setOnMouseDragExited(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) {
		 * node.setPortPressed(false);
		 * 
		 * } });
		 * 
		 * setOnMouseExited(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) {
		 * node.setPortPressed(false);
		 * 
		 * } }); ;
		 * 
		 * setOnMouseDragged(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { //
		 * System.out.println("hi"); node.setPortPressed(true);
		 * line.setStartX(x); line.setStartY(y);
		 * 
		 * if (event.isPrimaryButtonDown()) {
		 * line.endXProperty().bind(node.layoutXProperty().add(event.getSceneX()
		 * ));
		 * line.endYProperty().bind(node.layoutYProperty().add(event.getSceneY()
		 * )); }
		 * 
		 * } });
		 */

	}

	void mouseHandle(MouseEvent event, boolean press) {

		GNodeGraph graph = node.getNodeGraph();
		if (isInput()) {
			graph.getGuiMaster().setSecondPort(GPort.this);
		} else {
			graph.getGuiMaster().setFirstPort(GPort.this);
		}
		if (graph.getGuiMaster().connectPorts()) {
			graph.update();
			graph.getGuiMaster().removecurConnectPorts();
		}
		node.setPortPressed(press);

	}

	@SuppressWarnings("unused")
	private void mouseHandler() {
		this.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> mouseHandle(event, false));
		this.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> mouseHandle(event, false));
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

	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
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
