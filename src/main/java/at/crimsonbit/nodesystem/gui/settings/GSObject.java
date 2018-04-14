package at.crimsonbit.nodesystem.gui.settings;

public class GSObject extends GSBase {

	public Object setting;

	public GSObject(Object obj) {
		this.setting = obj;
	}

	public Object getSetting() {
		return setting;
	}

	public void setSetting(Object setting) {
		this.setting = setting;
	}

}
