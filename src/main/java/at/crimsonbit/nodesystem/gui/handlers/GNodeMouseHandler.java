package at.crimsonbit.nodesystem.gui.handlers;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.GState;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.util.DragContext;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * <h1>GNodeMouseHandler</h1>
 * <p>
 * This class is responsible for Node Handling. Especially the moving inside of
 * the NodeGraph
 * </p>
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

	public void makeMoveable(final Node n) {
		this.node = (GNode) n;

		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);

	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			GNode node = (GNode) event.getSource();
			if (event.isPrimaryButtonDown() && !node.getNodeGraph().getState().equals(GState.PORTCON)) {

				if (event.getClickCount() == 2) {
					node.toggleDraw();
					node.redraw(true);
				} else {
					if (!(node.isPortPressed())) {
						node.getNodeGraph().setState(GState.NODE_MOVE);
						for (GNode n : graph.getGuiMaster().getAllNodes()) {
							if (!n.equals(node)) {
								n.setActive(false);
								n.redraw();
							}
						}
						graph.setActive(node);
						node.toFront();
						node.redraw(true);

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
			GNode node = (GNode) event.getSource();
			if (!node.getNodeGraph().getState().equals(GState.PORTCON)) {
				if (!node.isPortPressed()) {
					if (event.isPrimaryButtonDown()) {
						node.getNodeGraph().setState(GState.NODE_MOVE);
						node.setCursor(Cursor.MOVE);

						double offsetX = event.getScreenX() + dragContext.x;
						double offsetY = event.getScreenY() + dragContext.y;

						// adjust the offset in case we are zoomed
						double scale = graph.getScaleValue();

						offsetX /= scale;
						offsetY /= scale;

						node.relocate(offsetX, offsetY);
						// node.redraw(true);
						node.getNodeGraph().update();
					}
				}
			}

		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			GNode node = (GNode) event.getSource();
			if (!node.getNodeGraph().getState().equals(GState.PORTCON)) {
				if (!(node.isPortPressed())) {
					// for (GNode n : graph.getGuiMaster().getAllCells()) {
					// if (!n.equals(node)) {
					// n.setActive(false);
					// n.redraw();
					// }
					// }
					// graph.setActive(node);
					node.redraw();
					graph.setState(GState.DEFAULT);
					node.setCursor(Cursor.DEFAULT);
				}

			}

		}
	};

}