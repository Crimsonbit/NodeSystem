package at.crimsonbit.nodesystem.gui.settings;

public class GSettingsEntry {

	private String stringID;
	private GSBase gsOBJ;

	public GSettingsEntry(GSBase node, String id) {
		this.gsOBJ = node;
		this.stringID = id;
	}

	public String getStringID() {
		return stringID;
	}

	public void setStringID(String stringID) {
		this.stringID = stringID;
	}

	public GSBase getObject() {
		return gsOBJ;
	}

	public void setObject(GSBase node) {
		this.gsOBJ = node;
	}

}
