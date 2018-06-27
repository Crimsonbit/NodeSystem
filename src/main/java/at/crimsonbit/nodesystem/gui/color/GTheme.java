package at.crimsonbit.nodesystem.gui.color;

import java.util.HashMap;

import at.crimsonbit.nodesystem.util.RangeMapper;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

/**
 * 
 * @author Florian Wagner
 *
 */
public class GTheme {

	private HashMap<GColors, Color> colorMap = new HashMap<GColors, Color>();
	private GStyle themeStyle = GStyle.GRADIENT;
	private static GTheme instance;

	public static int THEME_DARK = 0;
	public static int THEME_LIGHT = 1;

	private int theme = THEME_DARK;

	public static GTheme getInstance() {
		if (instance == null) {
			instance = new GTheme();
		}
		return instance;
	}

	private GTheme() {
		initColorTheme();
	}

	private void initColorTheme() {
		if (theme == 0) {
			getThemeColorMap().put(GColors.COLOR_NODE_ACTIVE, getColor(140d, 162d, 196d)); // new Color(0.992, 0.647,
			getThemeColorMap().put(GColors.COLOR_SELECTION, Color.LIGHTSKYBLUE);
			getThemeColorMap().put(GColors.COLOR_ACTIVE_TOGGLED, getColor(140d, 162d, 196d));
			getThemeColorMap().put(GColors.COLOR_PORT_INPUT, Color.CORNFLOWERBLUE);
			getThemeColorMap().put(GColors.COLOR_PORT_OUTPUT, Color.ORANGERED);
			getThemeColorMap().put(GColors.COLOR_CURVE_DEFAULT, Color.CRIMSON);
			getThemeColorMap().put(GColors.COLOR_TEXT_COLOR, Color.WHITE);
			getThemeColorMap().put(GColors.COLOR_SHADOW_COLOR, getColor(0.1d));
			getThemeColorMap().put(GColors.COLOR_BACKGROUND, getColor(32d));
			getThemeColorMap().put(GColors.COLOR_BACKGROUND_LINES, getColor(49d));
			getThemeColorMap().put(GColors.COLOR_NODE, getColor(10d));

		} else if (theme == 1) {
			getThemeColorMap().put(GColors.COLOR_NODE_ACTIVE, Color.LIGHTSKYBLUE); // new Color(0.992, 0.647, 0.305, 1)
			getThemeColorMap().put(GColors.COLOR_ACTIVE_TOGGLED, Color.LIGHTSKYBLUE);
			getThemeColorMap().put(GColors.COLOR_PORT_INPUT, Color.CORNFLOWERBLUE);
			getThemeColorMap().put(GColors.COLOR_PORT_OUTPUT, Color.ORANGERED);
			getThemeColorMap().put(GColors.COLOR_CURVE_DEFAULT, Color.CRIMSON);
			getThemeColorMap().put(GColors.COLOR_TEXT_COLOR, Color.WHITE);
			getThemeColorMap().put(GColors.COLOR_BACKGROUND, getColor(168d));
			getThemeColorMap().put(GColors.COLOR_BACKGROUND_LINES, getColor(105d));
			getThemeColorMap().put(GColors.COLOR_SHADOW_COLOR, getColor(0.1d));
			getThemeColorMap().put(GColors.COLOR_NODE, getColor(72d));
		}
	}

	public Color getColor(double c) {

		if (c > 1) {
			c /= 255d;
		}
		return new Color(c, c, c, 1);
	}

	public Color getColor(double c, double op) {

		if (c > 1) {
			c /= 255d;
		}
		return new Color(c, c, c, op);
	}

	public Color getColor(double c1, double c2, double c3, double op) {

		if (c1 > 1) {
			c1 /= 255d;
		}
		if (c2 > 1) {
			c2 /= 255d;
		}
		if (c3 > 1) {
			c3 /= 255d;
		}
		return new Color(c1, c2, c3, op);
	}

	public Color getColor(double c1, double c2, double c3) {

		if (c1 > 1) {
			c1 /= 255d;
		}
		if (c2 > 1) {
			c2 /= 255d;
		}
		if (c3 > 1) {
			c3 /= 255d;
		}
		return new Color(c1, c2, c3, 1);
	}

	public void setOpacity(GColors color, double op) {
		Color c = getColor(color);
		putColor(color, new Color(c.getRed(), c.getGreen(), c.getBlue(), op));
	}

	public Color getColorWithOpacity(GColors color, double op) {
		Color c = getColor(color);
		return new Color(c.getRed(), c.getGreen(), c.getBlue(), op);
	}

	public void setTheme(int i) {
		theme = i;
	}

	public void setStyle(GStyle style) {
		this.themeStyle = style;
	}

	public GStyle getStyle() {
		return this.themeStyle;
	}

	public Color getColor(GColors color) {
		return colorMap.get(color);
	}

	public void putColor(GColors string, Color c) {
		this.colorMap.put(string, c);
	}

	public HashMap<GColors, Color> getThemeColorMap() {
		return this.colorMap;
	}

	public LinearGradient getGradient(Color color) {
		Stop[] stops = new Stop[] { new Stop(0, color), new Stop(1, Color.TRANSPARENT) };
		LinearGradient lg1 = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE, stops);
		return lg1;
	}

}
