package at.crimsonbit.nodesystem.gui.settings;

public class GSShort extends GSBase {

	private short setting;

	public GSShort(short s) {
		this.setting = s;
	}

	public Object getSetting() {
		return setting;
	}

	public void setSetting(short setting) {
		this.setting = setting;
	}

}
