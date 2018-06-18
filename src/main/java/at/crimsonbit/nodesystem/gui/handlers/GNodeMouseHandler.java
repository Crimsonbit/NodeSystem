package at.crimsonbit.nodesystem.gui.handlers;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GState;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.util.DragContext;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author Florian Wagner
 *
 */
public class GNodeMouseHandler {

	private final DragContext dragContext = new DragContext();
	private GNodeGraph graph;
	private GNode node;

	public GNodeMouseHandler(GNodeGraph g) {
		this.graph = g;
	}

	public void addMouseHandler(final Node node) {
		this.node = (GNode) node;
		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);

	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			if (event.isPrimaryButtonDown() && !node.getNodeGraph().getState().equals(GState.PORTCON)) {
				
				if (event.getClickCount() == 2) {
					GNode node = (GNode) event.getSource();
					node.toggleDraw();
					node.redraw(true);

				} else {
					GNode node = (GNode) event.getSource();
					if (!(node.isPortPressed())) {
						node.getNodeGraph().setState(GState.NODE_MOVE);
						graph.setActive(node);
						node.setActive(true);

						node.redraw(true);
						node.toFront();

						double scale = graph.getScaleValue();
						dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
						dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();

						node.setCursor(Cursor.HAND);
					}
				}
			}
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			if (!node.getNodeGraph().getState().equals(GState.PORTCON)) {
				GNode n = (GNode) event.getSource();
				if (!n.isPortPressed()) {
					if (event.isPrimaryButtonDown()) {
						node.getNodeGraph().setState(GState.NODE_MOVE);
						Node node = (Node) event.getSource();

						double offsetX = event.getScreenX() + dragContext.x;
						double offsetY = event.getScreenY() + dragContext.y;

						// adjust the offset in case we are zoomed
						double scale = graph.getScaleValue();

						offsetX /= scale;
						offsetY /= scale;

						node.relocate(offsetX, offsetY);
						node.setCursor(Cursor.MOVE);
						n.getNodeGraph().update();
					}
				}
			}
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			if (!node.getNodeGraph().getState().equals(GState.PORTCON)) {
				// makeDraggable(node);
				GNode node = (GNode) event.getSource();

				if (!(node.isPortPressed())) {
					for (GNode n : graph.getGuiMaster().getAllCells()) {
						n.setActive(false);
						n.redraw();
						addMouseHandler(n);
					}
					node.setActive(true);
					node.redraw();
					graph.setActive(node);
					node.setCursor(Cursor.DEFAULT);
					node.getNodeGraph().setState(GState.DEFAULT);
				}
			}
		}
	};

}