package at.crimsonbit.nodesystem.gui.settings;

public class GSChar extends GSBase{

	private char setting;

	public GSChar(char c) {
		this.setting = c;
	}

	public Object getSetting() {
		return setting;
	}

	public void setSetting(char setting) {
		this.setting = setting;
	}

}
