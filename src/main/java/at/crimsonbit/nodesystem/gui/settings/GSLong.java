package at.crimsonbit.nodesystem.gui.settings;

public class GSLong extends GSBase {

	private long setting;

	public GSLong(long l) {
		this.setting = l;
	}

	public Object getSetting() {
		return setting;
	}

	public void setSetting(long setting) {
		this.setting = setting;
	}

}
