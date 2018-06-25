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
 * 
 * @author Florian Wagner
 *
 */
public class GNodeMouseHandler {

	private final DragContext dragContext = new DragContext();
	private GNodeGraph graph;
	private GNode node;
	private double y;
	private double x;
	private boolean initMinHeight;
	private boolean initMinWidth;
	private boolean draggableZoneX, draggableZoneY;
	private boolean dragging;
	private static final int RESIZE_MARGIN = 10;

	public GNodeMouseHandler(GNodeGraph g) {
		this.graph = g;
	}

	public void addMouseHandler(final Node node) {
		this.node = (GNode) node;

		node.setOnMouseMoved(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				mouseOver(event);
			}
		});

		node.setOnMousePressed(onMousePressedEventHandler);
		node.setOnMouseDragged(onMouseDraggedEventHandler);
		node.setOnMouseReleased(onMouseReleasedEventHandler);

	}

	protected boolean isInDraggableZone(MouseEvent event) {
		draggableZoneY = (boolean) (event.getY() > (node.getHeight() - RESIZE_MARGIN));
		draggableZoneX = (boolean) (event.getX() > (node.getWidth() - RESIZE_MARGIN));
		return (draggableZoneY || draggableZoneX);
	}

	EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			if (isInDraggableZone(event) || dragging) {
				dragging = true;

				graph.setState(GState.RESIZE);
				GNode node = (GNode) event.getSource();
				// make sure that the minimum height is set to the current height once,
				// setting a min height that is smaller than the current height will
				// have no effect
				if (!initMinHeight) {
					node.getDrawPane().setMinHeight(node.getHeight());
					initMinHeight = true;
				}

				y = event.getY();

				if (!initMinWidth) {
					node.getDrawPane().setMinWidth(node.getWidth());
					initMinWidth = true;
				}

				x = event.getX();

			} else {

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

		}
	};

	protected void mouseOver(MouseEvent event) {
		if (isInDraggableZone(event) || dragging) {
			if (draggableZoneY) {
				node.setCursor(Cursor.S_RESIZE);
			}

			if (draggableZoneX) {
				node.setCursor(Cursor.E_RESIZE);
			}

		} else {
			node.setCursor(Cursor.DEFAULT);
		}
	}

	EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			if (dragging) {
				if (draggableZoneY) {
					GNode node = (GNode) event.getSource();
					double mousey = event.getY();

					double newHeight = node.getDrawPane().getMinHeight() + (mousey - y);

					node.getDrawPane().setMinHeight(newHeight);
					// node.setPrefHeight(newHeight);
					y = mousey;
				}

				if (draggableZoneX) {
					double mousex = event.getX();

					double newWidth = node.getDrawPane().getMinWidth() + (mousex - x);

					node.getDrawPane().setMinWidth(newWidth);
					// node.setPrefWidth(newWidth);
					x = mousex;
				}
			} else if (!node.getNodeGraph().getState().equals(GState.PORTCON)) {
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
			GNode node = (GNode) event.getSource();
			node.redraw(true);
		}
	};

	EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {
			dragging = false;
			node.setCursor(Cursor.DEFAULT);
			GNode node = (GNode) event.getSource();
			node.redraw(true);
			if (!node.getNodeGraph().getState().equals(GState.PORTCON)) {
				// makeDraggable(node);

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
			graph.setState(GState.DEFAULT);
		}
	};

}