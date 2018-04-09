package at.crimsonbit.nodesystem.gui.node.port;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
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

	private void addPopUpHandler(GPopUp dialog) {
		this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
			dialog.show(this, event.getScreenX(), event.getScreenY());
			event.consume();
		});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			dialog.hide();
		});

		for (MenuItem item : this.dialog.getItems()) {
			int id = Integer.valueOf(item.getId());
			item.setOnAction(event -> {
				consumeMessage(id, (GEntry) item);
				event.consume();
			});
		}

		// addSubeMenuHandlers();

		/*
		 * for (MenuItem item : this.popUpDialog.getItems()) { int id =
		 * Integer.valueOf(item.getId()); item.setOnAction(event -> { //
		 * System.out.println(id); consumeMessage(id); event.consume(); });
		 * 
		 * }
		 */
	}

	public GPort(int id, boolean input, String labels, double x, double y, GNode node) {
		this.node = node;
		this.id = id;
		this.stringID = labels;
		this.input = input;
		this.x = x;
		this.y = y;
		GPortLabel label = new GPortLabel(x, y, labels, input);
		GPortRect rect = new GPortRect(x, y, input, node);

		getChildren().add(label);
		getChildren().add(rect);
		GPopUp pop = new GPopUp();
		pop.addItem(-1, labels, true);
		pop.addItem(0, "Connect");
		pop.addItem(1, "Disconnect");
		this.dialog = pop;
		addPopUpHandler(pop);

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
