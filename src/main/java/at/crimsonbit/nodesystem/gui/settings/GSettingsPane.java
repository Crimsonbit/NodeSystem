package at.crimsonbit.nodesystem.gui.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * GSettingsPane.
 * 
 * This pane holds the NodeField values (settings) of the node.
 * 
 * @author NeonArtworks
 *
 */
public class GSettingsPane extends Pane {
	/*
	private GNode currentNode;
	private GNodeGraph nodeGraph;
	private Group drawGroup = new Group();
	private List<GSettingsEntry> settingsEntries;
	private Rectangle rect;

	private boolean doDraw = true;
	private GSApply applyButton;

	public GSettingsPane(GNodeGraph graph) {
		this.settingsEntries = new ArrayList<GSettingsEntry>();
		this.nodeGraph = graph;
		getChildren().add(drawGroup);
	}

	public boolean isDraw() {
		return doDraw;
	}

	public void setDraw(boolean doDraw) {
		this.doDraw = doDraw;
	}

	public GNodeGraph getNodeGraph() {
		return nodeGraph;
	}

	public void setNodeGraph(GNodeGraph nodeGraph) {
		this.nodeGraph = nodeGraph;
	}

	public void setNode(GNode node) {
		currentNode = node;
	}

	public GNode getNode() {
		return this.currentNode;
	}

	private void drawBackground() {
		rect = new Rectangle(350, 500);
		BoxBlur blur = new BoxBlur(1, 1, 1);
		rect.setEffect(blur);
		rect.setFill(new Color(0, 0, 0, 0.3));
		drawGroup.getChildren().add(rect);
	}

	private void redraw(boolean draw) {
		setDraw(draw);
		draw();
		// setDraw(!draw);
	}

	public void redraw() {
		redraw(false);
		redraw(true);
	}

	private void getFieldEntries() {
		if (currentNode != null) {
			settingsEntries.clear();
			Set<String> fieldNames;
			fieldNames = nodeGraph.getGuiMaster().getNodeMaster().getAllFieldNames(currentNode.getNode());
			for (String s : fieldNames) {
				Object d = null;
				try {
					d = nodeGraph.getGuiMaster().getNodeMaster().getFieldType(currentNode.getNode(), s);
				} catch (NoSuchNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				GSBase obj = null;
				if (d != null) {

					if (d instanceof String) {
						obj = new GSString((String) currentNode.getNode().get(s));

					} else if (d instanceof Float) {
						obj = new GSFloat((float) currentNode.getNode().get(s));

					} else if (d instanceof Double) {
						obj = new GSDouble((double) currentNode.getNode().get(s));

					} else if (d instanceof Long) {
						obj = new GSLong((long) currentNode.getNode().get(s));

					} else if (d instanceof Short) {
						obj = new GSShort((short) currentNode.getNode().get(s));

					} else {
						obj = new GSObject((Object) currentNode.getNode().get(s));

					}
					if (obj != null) {
						GSettingsEntry entry = new GSettingsEntry(obj, s);
						settingsEntries.add(entry);
					}
				}
			}
		}
	}

	public void draw() {

		if (doDraw) {
			getFieldEntries();
			drawBackground();
			for (GSettingsEntry entry : settingsEntries) {

				GSBase setting = entry.getObject();

				if (setting instanceof GSObject) {

				}
			}

			applyButton = new GSApply("Apply Settings");
			applyButton.relocate(0, 500);
			applyButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// for (GSettingsEntry e : settingsEntries) {
					//
					// }
				}

			});

			drawGroup.getChildren().add(applyButton);
		} else {
			drawGroup.getChildren().clear();
		}
	}
	*/
}
