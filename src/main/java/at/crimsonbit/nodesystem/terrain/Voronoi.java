package at.crimsonbit.nodesystem.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Random;
/**
 * 
 * Simple class that generates Voronoi patterns
 * 
 * @author Florian Wagner
 *
 */
public class Voronoi {

	private int sizeX;
	private int sizeY;
	private int cells;
	private int colorvec = 0;
	private BufferedImage Buff;
	private int[] px, py, color;
	private Random rand = new Random();

	public Voronoi(int x, int y, int cella) {

		sizeX = x;
		sizeY = y;
		cells = cella;

		Buff = new BufferedImage(x, y, BufferedImage.TYPE_INT_RGB);

		px = new int[cells];
		py = new int[cells];
		color = new int[cells];

		if (colorvec == 0) {
			colorvec = 16226277;
		}

		for (int i = 0; i < cells; i++) {
			px[i] = rand.nextInt(sizeY);
			py[i] = rand.nextInt(sizeX);
			color[i] = rand.nextInt(255);
		}

	}

	private double generateDistance(int x1, int x2, int y1, int y2) {
		return Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
	}

	public BufferedImage getVoronoi() {

		int n;
		for (int x = 0; x < sizeY; x++) {
			for (int y = 0; y < sizeX; y++) {
				n = 0;
				for (int i = 0; i < cells; i++) {
					if (generateDistance(px[i], x, py[i], y) < generateDistance(px[n], x, py[n], y)) {
						n = i;
					}
				}
				Buff.setRGB(x, y, new Color(color[n],color[n],color[n]).getRGB());
			}
		}
		return Buff;
	}

}