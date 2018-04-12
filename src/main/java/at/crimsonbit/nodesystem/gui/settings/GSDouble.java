package at.crimsonbit.nodesystem.gui.settings;

public class GSDouble extends GSBase {

	private double setting;

	public GSDouble(double d) {
		this.setting = d;
	}

	public Object getSetting() {
		return setting;
	}

	public void setSetting(double setting) {
		this.setting = setting;
	}

}
