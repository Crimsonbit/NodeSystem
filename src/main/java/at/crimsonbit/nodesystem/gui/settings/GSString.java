package at.crimsonbit.nodesystem.gui.settings;

public class GSString extends GSBase {

	private String setting;

	public GSString(String s) {
		this.setting = s;
	}

	public String getSetting() {
		return setting;
	}

	public void setSetting(String setting) {
		this.setting = setting;
	}

}
