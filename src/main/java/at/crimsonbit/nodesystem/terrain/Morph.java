package at.crimsonbit.nodesystem.terrain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

import at.crimsonbit.nodesystem.util.ImageUtils;
import at.crimsonbit.nodesystem.util.OpenSimplexNoise;

public class Morph implements IMorph {

	private static Random r = new Random();

	public static BufferedImage PyErode(BufferedImage in, int n, int nl, double ev, int maxx, int maxy) {
		Color c;
		Color c2;
		int iv;
		int cv;
		int cx = 0;
		int cy = 0;
		BufferedImage out = in;
		for (int k = 0; k < n; n++) {
			cx = r.nextInt(maxx - 1);
			cy = r.nextInt(maxy - 1);
			for (int l = 0; l < nl; l++) {

				c = new Color(in.getRGB(cx, cy));
				cv = c.getRed();
				int mini = cx;
				int minj = cy;
				int miv = c.getRed();
				for (int i = 0; i < 3; i++)
					for (int j = 0; j < 3; j++) {
						int t1 = cx + i;
						int t2 = cy + j;
						// System.out.println(t1);
						// System.out.println(t2);
						c2 = new Color(in.getRGB(t1, t2));
						iv = c2.getRed();
						if (c2.getRed() < miv) {
							mini = cx + i - 1;
							minj = cy + j - 1;
							miv = c.getRed();
						}
					}
				if (miv < c.getRed()) {
					int hard = (int) ((256.0 - c.getRed()) / 256d);
					double erode_val = ev * hard;
					iv = (int) (miv + (cv - miv) * erode_val);
					cv = (int) (cv - (cv - miv) * erode_val);
					Color x = new Color(cv, cv, cv);
					Color y = new Color(iv, iv, iv);
					out.setRGB(cx, cy, x.getRGB());
					out.setRGB(mini, minj, y.getRGB());
					cx = mini;
					cy = minj;
				} else
					break;
				if (cx <= 0)
					break;
				if (cy <= 0)
					break;
				if (cx >= maxx - 1)
					break;
				if (cy >= maxy - 1)
					break;

			}
		}
		return out;
	}

	public static BufferedImage SErode(BufferedImage img, int n) {
		try (Morph morph = new Morph()) {
			return morph.Erode(img, n);
		}
	}

	public static BufferedImage SDilate(BufferedImage img, int n) {
		try (Morph morph = new Morph()) {
			return morph.Dilate(img, n);
		}
	}

	public BufferedImage Erode(BufferedImage img, int n) {
		/**
		 * Dimension of the image img.
		 */
		int width = img.getWidth();
		int height = img.getTileHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// buff

		// perform erosion
		// buff = new int[100];
		int[] imgData = ImageUtils.getData(img);
		for (int j = 0; j < n; j++) {
			int[] outRGBBuffer = ((DataBufferInt) (out.getRaster().getDataBuffer())).getData();
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					outRGBBuffer[x + (y * width)] = findMinInRange(imgData, width, height, y, x);
				}
			}

			/**
			 * Save the erosion value in image img.
			 */

			System.arraycopy(outRGBBuffer, 0, imgData, 0, img.getHeight() * img.getWidth());
		}
		return out;
	}

	protected static int findMinInRange(int[] imgData, int width, int height, int y, int x) {
		int smallest = 0xFFFFFFFF;
		for (int ty = y - 1; ty <= y + 1; ty++) {
			for (int tx = x - 1; tx <= x + 1; tx++) {
				/**
				 * 3x3 mask [kernel or structuring element] [1, 1, 1 1, 1, 1 1, 1, 1]
				 */
				if (ty >= 0 && ty < height && tx >= 0 && tx < width) {
					// pixel under the mask
					int c = imgData[tx + (ty * width)];
					if (Integer.compareUnsigned(c, smallest) < 0)
						smallest = c;
					// buff[i] = c & 0xFF;
				}
			}
		}

		// save lowest value
		return smallest;
	}

	protected static int findMaxInRange(int[] imgData, int width, int height, int y, int x) {
		int biggest = 0;
		for (int ty = y - 1; ty <= y + 1; ty++) {
			for (int tx = x - 1; tx <= x + 1; tx++) {
				/**
				 * 3x3 mask [kernel or structuring element] [1, 1, 1 1, 1, 1 1, 1, 1]
				 */
				if (ty >= 0 && ty < height && tx >= 0 && tx < width) {
					// pixel under the mask
					int c = imgData[tx + (ty * width)];
					if (Integer.compareUnsigned(c, biggest) > 0)
						biggest = c;
					// buff[i] = c & 0xFF;
				}
			}
		}

		// save lowest value
		return biggest;
	}

	public BufferedImage Dilate(BufferedImage img, int n) {
		/**
		 * Dimension of the image img.
		 */
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// output of dilation
		int[] imgData = ImageUtils.getData(img);
		for (int j = 0; j < n; j++) {
			int[] outRGBBuffer = ((DataBufferInt) (out.getRaster().getDataBuffer())).getData();

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					outRGBBuffer[x + (y * width)] = Morph.findMaxInRange(imgData, width, height, y, x);
				}
			}

			System.arraycopy(outRGBBuffer, 0, imgData, 0, img.getHeight() * img.getWidth());
		}
		return out;
	}

	public static BufferedImage Erode2(BufferedImage img, double smoothness) {
		BufferedImage out = img;
		int size = img.getWidth();
		int[][] heightmap = ImageUtils.retrivePixels(img, size, size);
		for (int i = 1; i < size - 1; i++) {
			for (int j = 1; j < size - 1; j++) {
				double d_max = 0.0f;
				int match[] = { 0, 0 };

				for (int u = -1; u <= 1; u++) {
					for (int v = -1; v <= 1; v++) {
						if (Math.abs(u) + Math.abs(v) > 0) {
							double d_i = heightmap[i][j] - heightmap[i + u][j + v];
							if (d_i > d_max) {
								d_max = d_i;
								match[0] = u;
								match[1] = v;
							}
						}
					}
				}

				if (0 < d_max && d_max <= (smoothness / (double) size)) {
					double d_h = 0.5f * d_max;
					heightmap[i][j] -= d_h;
					heightmap[i + match[0]][j + match[1]] += d_h;
				}
			}
		}

		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				out.setRGB(x, y, heightmap[x][y]);
			}

		return out;
	}

	public static BufferedImage Perturb(BufferedImage img, double f, double d) {
		BufferedImage out = img;
		int size = img.getWidth();
		OpenSimplexNoise noise = new OpenSimplexNoise();
		int[][] heightmap = ImageUtils.retrivePixels(img, size, size);

		int u, v;
		int[][] temp = new int[size][size];

		for (int i = 0; i < size; ++i) {
			for (int j = 0; j < size; ++j) {
				u = i + (int) (noise.eval(f * i / (double) size, f * j / (double) size, 0) * d);
				v = j + (int) (noise.eval(f * i / (double) size, f * j / (double) size, 1) * d);
				if (u < 0) {
					u = 0;
				}
				if (u >= size) {
					u = size - 1;
				}
				if (v < 0) {
					v = 0;
				}
				if (v >= size) {
					v = size - 1;
				}
				temp[i][j] = heightmap[u][v];
			}
		}
		heightmap = temp;
		int[] outRGBBuffer = ((DataBufferInt) (out.getRaster().getDataBuffer())).getData();
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				outRGBBuffer[y * size + x] = heightmap[x][y];
			}
		return out;
	}

	@Override
	public void close() {

	}

}