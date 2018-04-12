package at.crimsonbit.nodesystem.gui.settings;

import at.crimsonbit.nodesystem.gui.node.GNode;

public class GSettingsEntry {

	private String stringID;
	private GNode node;

	public GSettingsEntry(GNode node, String id) {
		this.node = node;
		this.stringID = id;
	}

	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public GNode getNode() {
		return node;
	}

	public void setNode(GNode node) {
		this.node = node;
	}

}
