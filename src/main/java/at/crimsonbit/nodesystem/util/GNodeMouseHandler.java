package at.crimsonbit.nodesystem.util;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * 
 * @author NeonArtworks
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
			if (event.isPrimaryButtonDown()) {
				GNode node = (GNode) event.getSource();
				if (!(node.isPortPressed())) {
					graph.setActive(node);
					node.setActive(true);
					node.redraw(true);
					node.toFront();

					double scale = graph.getScaleValue();
					dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
					dragContext.y = node.getBoundsInParent().getMinY() * scale - event.getScreenY();
				}
			}
		}
	};

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			GNode n = (GNode) event.getSource();
			if (!n.isPortPressed()) {
				if (event.isPrimaryButtonDown()) {
					Node node = (Node) event.getSource();

					double offsetX = event.getScreenX() + dragContext.x;
					double offsetY = event.getScreenY() + dragContext.y;

					// adjust the offset in case we are zoomed
					double scale = graph.getScaleValue();

					offsetX /= scale;
					offsetY /= scale;

					node.relocate(offsetX, offsetY);
				}
			}
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {

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
			}
		}
	};

}