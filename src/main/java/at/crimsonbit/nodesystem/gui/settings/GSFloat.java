package at.crimsonbit.nodesystem.gui.settings;

public class GSFloat extends GSBase {

	private float setting;

	public GSFloat(float f) {
		this.setting = f;
	}

	public Object getSetting() {
		return setting;
	}

	public void setSetting(float setting) {
		this.setting = setting;
	}

}
