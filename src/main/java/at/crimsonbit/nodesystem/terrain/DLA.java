package at.crimsonbit.nodesystem.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class DLA {

	public static BufferedImage genDLA(int size, int rad) {
		int N = size;
		int x, y;
		double dist;
		boolean[][] dla = new boolean[N][N];
		double[][] map = new double[N][N];
		double radius = rad;
		BufferedImage pic = new BufferedImage(N, N, BufferedImage.TYPE_INT_ARGB);
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				map[i][j] = 0;
			}
		}

		dla[N / 2][N / 2] = true;

		while (radius < (N / 2 - 2)) {

			// choose launching site on circle of given radius from seed
			double angle = 2.0 * Math.PI * Math.random();
			x = (int) (N / 2.0 + radius * Math.cos(angle));
			y = (int) (N / 2.0 + radius * Math.sin(angle));

			// particle takes a 2d random walk
			while (true) {
				double r = Math.random();
				if (r < 0.25)
					x--;
				else if (r < 0.50)
					x++;
				else if (r < 0.75)
					y++;
				else
					y--;

				// check if entered kill zone
				dist = Math.sqrt((N / 2 - x) * (N / 2 - x) + (N / 2 - y) * (N / 2 - y));
				if (dist >= Math.min((N - 2) / 2.0, radius + 25))
					break;

				// check if neighboring site is occupied
				if (dla[x - 1][y] || dla[x + 1][y] || dla[x][y - 1] || dla[x][y + 1] || dla[x - 1][y - 1]
						|| dla[x + 1][y + 1] || dla[x - 1][y + 1] || dla[x + 1][y - 1]) {
					dla[x][y] = true;
					if (dist > radius)
						radius = dist;
					break;
				}
			}

			if (dla[x][y]) {
				pic.setRGB(x, y, new Color(255, 255, 255).getRGB());

			}
		}

		return pic;
	}
}