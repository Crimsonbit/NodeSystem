package at.crimsonbit.nodesystem.gui.settings;

import java.util.HashMap;

public class GGraphSettings {

	private HashMap<GSettings, Object> settings = new HashMap<GSettings, Object>();
	private static GGraphSettings instance;

	public static GGraphSettings getInstance() {
		if (instance == null) {
			instance = new GGraphSettings();
		}
		return instance;
	}

	private GGraphSettings() {
		init();
	}

	private void init() {
		set(GSettings.SETTING_CURVE_WIDTH, 6d);
		set(GSettings.SETTING_CURVE_CURVE, 100d);
		set(GSettings.SETTING_SHADOW_WIDTH, 5d);
		set(GSettings.SETTING_SHADOW_HEIGHT, 5d);
		set(GSettings.SETTING_SHADOW_RADIUS, 20d);
		set(GSettings.SETTING_SHOW_CONNECTION_HELP, true);
		set(GSettings.SETTING_BIG_LINES_SPACING, 10);
	}

	/**
	 * <h1>public void set({@link GSettings}, {@link Object})</h1>
	 * <hr>
	 * <p>
	 * Using this method you can change a almost every setting, which is not
	 * hardcoded into the nodesystem. To see all settings please have a look at
	 * {@link GSettings}.
	 * </p>
	 * 
	 * @param s
	 *            the setting you want to change
	 * @param r
	 *            the value, please keep in mind, that setting settings to random
	 *            stuff can break the nodesystem and it can throw unexpected errors.
	 */
	public void set(GSettings s, Object r) {
		this.settings.put(s, r);

	}

	/**
	 * <h1>public {@link HashMap}<{@link GSettings}, {@link Object}>
	 * getSettings()</h1>
	 * <p>
	 * Returns the HashMap containing all the settings for the graph and their
	 * corresponding values.
	 * </p>
	 * 
	 * @return the settings of the graph
	 */
	public HashMap<GSettings, Object> getSettings() {
		return this.settings;
	}

	public Object getSetting(GSettings setting) {
		return this.settings.get(setting);
	}

}
