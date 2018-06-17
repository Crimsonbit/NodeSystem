package at.crimsonbit.nodesystem.gui.node.port;

import java.util.Set;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GNodeMaster;
import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

/**
 * 
 * @author Florian Wagner
 *
 */

public class GPort extends Group implements IGConsumable {

	private double x;
	private double y;
	private int id;
	private boolean input;
	private GNode node;
	private Line line;
	private GPopUp dialog;
	private String stringID;
	private GPortLabel label;
	private GPortRect rect;
	private boolean drawText = true;
	private final Tooltip tooltip = new Tooltip();
	private final int MAGIC_OFFSET = 3;
	private final int SNAP_SIZE = 5;
	private boolean isConnected = false;

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

		setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				node.setPortPressed(false);

				/* Add Code to connect nodes here */
				boolean isInput = false;
				boolean isOutput = false;
				GNodeMaster master = node.getNodeGraph().getGuiMaster();
				master.removecurConnectPorts();

				if (!input) {
					master.setFirstPort(GPort.this);
					isOutput = true;
				} else {
					master.setSecondPort(GPort.this);
					isInput = true;
				}
				Set<GNode> allNodes = node.getNodeGraph().getGuiMaster().getAllCells();
				for (GNode n : allNodes) {
					if (isInput) {
						for (GPort p : n.getOutputPorts()) {
							if (Math.abs(event.getX() - p.getX()) > SNAP_SIZE) {
								master.setFirstPort(p);
								master.connectPorts();
							}
						}
					} else {
						for (GPort p : n.getInputPorts()) {
							if (Math.abs(event.getX() - p.getX()) > SNAP_SIZE) {
								master.setSecondPort(p);
								master.connectPorts();
							}
						}
					}

				}

				master.removecurConnectPorts();

				/* Remove Temporary Line */
				node.getNodeGraph().getLineLayer().getChildren().remove(line);
				node.getNodeGraph().update();
			}
		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) { // System.out.println("hi");
				node.setPortPressed(true);
				if (line == null)
					line = new Line();

				line.setStroke(node.getNodeGraph().getColorLookup().get(node.getNodeType()));
				line.setStrokeWidth((double) node.getNodeGraph().getSettings().get(GraphSettings.SETTING_CURVE_WIDTH));
				line.startXProperty().bind(node.layoutXProperty().add(getPortX() + MAGIC_OFFSET));
				line.startYProperty().bind(node.layoutYProperty().add(getY() + MAGIC_OFFSET));

				line.setStrokeLineCap(StrokeLineCap.ROUND);
				line.setFill(Color.TRANSPARENT);

				line.endXProperty().bind(node.layoutXProperty().add(event.getX()));
				line.endYProperty().bind(node.layoutYProperty().add(event.getY()));

				node.getNodeGraph().getLineLayer().getChildren().remove(line);
				node.getNodeGraph().getLineLayer().getChildren().add(line);
				node.getNodeGraph().update();
			}

		});

	}

	public boolean isConnected() {
		return isConnected;
	}

	public void setConnected(boolean isConnected) {
		this.isConnected = isConnected;
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

	public void setDrawText(boolean f) {
		this.drawText = f;
	}

	public void draw() {
		if (drawText)
			getChildren().add(label);
		getChildren().add(rect);
	}

	public void redraw() {
		getChildren().clear();
		if (drawText)
			getChildren().add(label);
		getChildren().add(rect);
	}

	public double getPortX() {
		return this.x;
	}

	public void relocatePortX(double x) {
		// getChildren().clear();
		label.setIx(x);
		rect.setRX(x);
		this.x = x;
		// relocate(x);
		// getChildren().add(label);
		// getChildren().add(rect);
		// draw();
	}

	public void relocatePortY(double y) {
		label.setIy(y);
		rect.setRY(y);
		this.y = y;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		result = prime * result + ((stringID == null) ? 0 : stringID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GPort other = (GPort) obj;
		if (id != other.id)
			return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		if (stringID == null) {
			if (other.stringID != null)
				return false;
		} else if (!stringID.equals(other.stringID))
			return false;
		return true;
	}

}
