package at.crimsonbit.nodesystem.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import at.crimsonbit.nodesystem.util.BaseContainer;
/**
 * 
 * @author Florian Wagner
 *
 */
public class Displacement {

	private BaseContainer bc = new BaseContainer(0);

	private int width, height;
	private double contrast, corner, brightness, seed;
	private double distance;
	private double[][] map;
	private Random random = new Random();
	private String method;
	Random r = new Random();

	public Displacement() {
		initBaseContainer();
		setValues();

		map = new double[width][height];
	}

	public void setSettings(BaseContainer bc) {
		this.bc = bc;
		setValues();
	}

	private void setValues() {
		width = (int) bc.getEntryById("DISP_WIDTH");
		height = (int) bc.getEntryById("DISP_HEIGHT");
		contrast = (int) bc.getEntryById("DISP_CONTRAST");
		corner = (int) bc.getEntryById("DISP_CORNER");
		brightness = (double) bc.getEntryById("DISP_BRIGHTNESS");
		distance = (double) bc.getEntryById("DISP_DISTANCE");
		seed = (int) bc.getEntryById("DISP_SEED");
		r.setSeed((long) seed);
	}

	private void init() {

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				map[x][y] = 0;
			}
		}

		map[0][0] = corner;
		map[width - 1][0] = corner;
		map[width - 1][height - 1] = corner;
		map[0][height - 1] = corner;

		midpoint(0, 0, width - 1, height - 1);
	}

	private boolean midpoint(int x1, int y1, int x2, int y2) {

		if (x2 - x1 < 2 && y2 - y1 < 2)
			return false;
		double dist;
		double a = 0.54658;
		if (method.equals("a"))
			dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		// dist = (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)) * (1 -
		// a)
		// + (Math.abs(x2 - x1) + Math.abs(y2 - y1)) * a);
		// int dist = (int) Math.abs(((int) Math.sqrt(x1 - x2) + Math.sqrt(y2 -
		// y1) * contrast));
		else
			// dist = Math.sqrt((x2 - x1) / 2 + (y2 + y1) / 2) * contrast;
			dist = Math.sqrt(Math.abs((x2 - x1) + y2 - y1) * contrast);
		// double dist = Math.sqrt(Math.abs(x1 - x2) + Math.abs(y1 -
		// y2) + Math.abs((x1 - x2) + (y1 - y2) + x2 + y2))*contrast; //

		//
		double hdist = dist / distance;

		int midx = (x1 + x2) / 2;
		int midy = (y1 + y2) / 2;

		double c1 = map[x1][y1];
		double c2 = map[x2][y1];
		double c3 = map[x2][y2];
		double c4 = map[x1][y2];

		if (map[midx][y1] == 0)
			map[midx][y1] = ((c1 + c2 + r.nextInt((int) dist) - hdist) / 2);
		if (map[midx][y2] == 0)
			map[midx][y2] = ((c4 + c3 + r.nextInt((int) dist) - hdist) / 2);
		if (map[x1][midy] == 0)
			map[x1][midy] = ((c1 + c4 + r.nextInt((int) dist) - hdist) / 2);
		if (map[x2][midy] == 0)
			map[x2][midy] = ((c2 + c3 + r.nextInt((int) dist) - hdist) / 2);

		map[midx][midy] = ((c1 + c2 + c3 + c4 + r.nextInt((int) dist) - hdist) / brightness);

		midpoint(x1, y1, midx, midy);
		midpoint(midx, y1, x2, midy);
		midpoint(x1, midy, midx, y2);
		midpoint(midx, midy, x2, y2);

		return true;
	}

	private void initBaseContainer() {
		bc.addEntry("DISP_CONTRAST", 10);
		bc.addEntry("DISP_CORNER", 64);
		bc.addEntry("DISP_BRIGHTNESS", 4d);
		bc.addEntry("DISP_DISTANCE", 2d);
		bc.addEntry("DISP_WIDTH", 1920);
		bc.addEntry("DISP_HEIGHT", 1080);
		bc.addEntry("DISP_SEED", random.nextInt(Integer.MAX_VALUE));
	}

	public BaseContainer getSettings() {

		bc.PrintEntries();

		return bc;

	}

	public BufferedImage createDisplacement(int width, int height, String m) {
		this.width = width;
		this.height = height;
		this.method = m;
		map = new double[width][height];
		init();

		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		int[] colorData = ((DataBufferInt) (img.getRaster().getDataBuffer())).getData();
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double col = map[x][y];
				if (col >= 255) {
					col = 230;
				}
				if (col <= 0) {
					col = 2;
				}
				colorData[y*width + x] = new Color((int) col, (int) col, (int) col).getRGB();
			}
		}

		return img;

	}
}