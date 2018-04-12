package at.crimsonbit.nodesystem.gui.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
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

	private GNode currentNode;
	private GNodeGraph nodeGraph;
	private Group drawGroup = new Group();
	private List<GSettingsEntry> settingsEntries;
	private Rectangle rect;

	private boolean baseShapeDrawn = false;

	public GSettingsPane(GNodeGraph graph) {
		this.settingsEntries = new ArrayList<GSettingsEntry>();
		this.nodeGraph = graph;
	}

	@Override
	public void layoutChildren() {

		draw();
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
		if (!baseShapeDrawn) {
			drawGroup.getChildren().add(rect);
			getChildren().add(drawGroup);
			baseShapeDrawn = true;
		}
	}

	private void getFieldEntries() {
		if (currentNode != null) {
			settingsEntries.clear();
			Set<String> fieldNames;
			fieldNames = nodeGraph.getGuiMaster().getNodeMaster().getAllFieldNames(currentNode.getNode());
			for (String s : fieldNames) {
				GSettingsEntry entry = new GSettingsEntry(currentNode, s);
				settingsEntries.add(entry);
			}
		}
	}

	public void draw() {
		getFieldEntries();
		drawBackground();
	}

}
