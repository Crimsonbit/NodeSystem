package at.crimsonbit.nodesystem.util;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Util class to handle window resizing when a Stage style set to
 * StageStyle.UNDECORATED. Created on 8/15/17.
 *
 * @author Evgenii Kanivets
 */
public class ResizeHelper {

	public static void addResizeListener(Stage Stage) {
		addResizeListener(Stage, 0, 0, Double.MAX_VALUE, Double.MAX_VALUE);
	}
	
	public static void addResizeListener(Stage Stage, double minWidth, double minHeight, double maxWidth,
			double maxHeight) {
		ResizeListener resizeListener = new ResizeListener(Stage);
		Stage.getScene().addEventHandler(MouseEvent.MOUSE_MOVED, resizeListener);
		Stage.getScene().addEventHandler(MouseEvent.MOUSE_PRESSED, resizeListener);
		Stage.getScene().addEventHandler(MouseEvent.MOUSE_DRAGGED, resizeListener);
		Stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED, resizeListener);
		Stage.getScene().addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, resizeListener);

		resizeListener.setMinWidth(minWidth);
		resizeListener.setMinHeight(minHeight);
		resizeListener.setMaxWidth(maxWidth);
		resizeListener.setMaxHeight(maxHeight);

		ObservableList<Node> children = Stage.getScene().getRoot().getChildrenUnmodifiable();
		for (Node child : children) {
			addListenerDeeply(child, resizeListener);
		}
	}

	private static void addListenerDeeply(Node node, EventHandler<MouseEvent> listener) {
		node.addEventHandler(MouseEvent.MOUSE_MOVED, listener);
		node.addEventHandler(MouseEvent.MOUSE_PRESSED, listener);
		node.addEventHandler(MouseEvent.MOUSE_DRAGGED, listener);
		node.addEventHandler(MouseEvent.MOUSE_EXITED, listener);
		node.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, listener);
		if (node instanceof Parent) {
			Parent parent = (Parent) node;
			ObservableList<Node> children = parent.getChildrenUnmodifiable();
			for (Node child : children) {
				addListenerDeeply(child, listener);
			}
		}
	}

	static class ResizeListener implements EventHandler<MouseEvent> {
		private Stage Stage;
		private Cursor cursorEvent = Cursor.DEFAULT;
		private int border = 4;
		private double startX = 0;
		private double startY = 0;

		// Max and min sizes for controlled Stage
		private double minWidth;
		private double maxWidth;
		private double minHeight;
		private double maxHeight;

		public ResizeListener(Stage Stage) {
			this.Stage = Stage;
		}

		public void setMinWidth(double minWidth) {
			this.minWidth = minWidth;
		}

		public void setMaxWidth(double maxWidth) {
			this.maxWidth = maxWidth;
		}

		public void setMinHeight(double minHeight) {
			this.minHeight = minHeight;
		}

		public void setMaxHeight(double maxHeight) {
			this.maxHeight = maxHeight;
		}

		public void handle(MouseEvent mouseEvent) {
			EventType<? extends MouseEvent> mouseEventType = mouseEvent.getEventType();
			Scene scene = Stage.getScene();

			double mouseEventX = mouseEvent.getSceneX(), mouseEventY = mouseEvent.getSceneY(),
					sceneWidth = scene.getWidth(), sceneHeight = scene.getHeight();

			if (MouseEvent.MOUSE_MOVED.equals(mouseEventType)) {
				if (mouseEventX < border && mouseEventY < border) {
					cursorEvent = Cursor.NW_RESIZE;
				} else if (mouseEventX < border && mouseEventY > sceneHeight - border) {
					cursorEvent = Cursor.SW_RESIZE;
				} else if (mouseEventX > sceneWidth - border && mouseEventY < border) {
					cursorEvent = Cursor.NE_RESIZE;
				} else if (mouseEventX > sceneWidth - border && mouseEventY > sceneHeight - border) {
					cursorEvent = Cursor.SE_RESIZE;
				} else if (mouseEventX < border) {
					cursorEvent = Cursor.W_RESIZE;
				} else if (mouseEventX > sceneWidth - border) {
					cursorEvent = Cursor.E_RESIZE;
				} else if (mouseEventY < border) {
					cursorEvent = Cursor.N_RESIZE;
				} else if (mouseEventY > sceneHeight - border) {
					cursorEvent = Cursor.S_RESIZE;
				} else {
					cursorEvent = Cursor.DEFAULT;
				}
				scene.setCursor(cursorEvent);
			} else if (MouseEvent.MOUSE_EXITED.equals(mouseEventType)
					|| MouseEvent.MOUSE_EXITED_TARGET.equals(mouseEventType)) {
				scene.setCursor(Cursor.DEFAULT);
			} else if (MouseEvent.MOUSE_PRESSED.equals(mouseEventType)) {
				startX = Stage.getWidth() - mouseEventX;
				startY = Stage.getHeight() - mouseEventY;
			} else if (MouseEvent.MOUSE_DRAGGED.equals(mouseEventType)) {
				if (!Cursor.DEFAULT.equals(cursorEvent)) {
					if (!Cursor.W_RESIZE.equals(cursorEvent) && !Cursor.E_RESIZE.equals(cursorEvent)) {
						double minHeight = Stage.getMinHeight() > (border * 2) ? Stage.getMinHeight() : (border * 2);
						if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.N_RESIZE.equals(cursorEvent)
								|| Cursor.NE_RESIZE.equals(cursorEvent)) {
							if (Stage.getHeight() > minHeight || mouseEventY < 0) {
								setStageHeight(Stage.getY() - mouseEvent.getScreenY() + Stage.getHeight());
								Stage.setY(mouseEvent.getScreenY());
							}
						} else {
							if (Stage.getHeight() > minHeight || mouseEventY + startY - Stage.getHeight() > 0) {
								setStageHeight(mouseEventY + startY);
							}
						}
					}

					if (!Cursor.N_RESIZE.equals(cursorEvent) && !Cursor.S_RESIZE.equals(cursorEvent)) {
						double minWidth = Stage.getMinWidth() > (border * 2) ? Stage.getMinWidth() : (border * 2);
						if (Cursor.NW_RESIZE.equals(cursorEvent) || Cursor.W_RESIZE.equals(cursorEvent)
								|| Cursor.SW_RESIZE.equals(cursorEvent)) {
							if (Stage.getWidth() > minWidth || mouseEventX < 0) {
								setStageWidth(Stage.getX() - mouseEvent.getScreenX() + Stage.getWidth());
								Stage.setX(mouseEvent.getScreenX());
							}
						} else {
							if (Stage.getWidth() > minWidth || mouseEventX + startX - Stage.getWidth() > 0) {
								setStageWidth(mouseEventX + startX);
							}
						}
					}
				}

			}
		}

		private void setStageWidth(double width) {
			width = Math.min(width, maxWidth);
			width = Math.max(width, minWidth);
			Stage.setWidth(width);
		}

		private void setStageHeight(double height) {
			height = Math.min(height, maxHeight);
			height = Math.max(height, minHeight);
			Stage.setHeight(height);
		}

	}
}