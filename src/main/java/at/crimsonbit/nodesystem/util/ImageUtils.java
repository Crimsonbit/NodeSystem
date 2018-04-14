package at.crimsonbit.nodesystem.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.GrayFilter;

/**
 * 
 * This is a rather old image-util class that I have made about a year ago.
 * 
 * @author NeonArtworks
 *
 */
public class ImageUtils {

	private Integer[][] pixels;
	private BufferedImage image;
	private BufferedImage gImage;
	@SuppressWarnings("unused")
	private String imagePath;
	private static int[][] matrix = new int[3][3];
	static double[][] kernelX = new double[][] { { 1, 0, -1 }, { 2, 0, -2 }, { 1, 0, -1 } };
	double[][] kernelY = new double[][] { { -1, -2, -1 }, { 0, 0, 0 }, { 1, 2, 1 } };

	public ImageUtils() {

	}

	public static BufferedImage zoom(BufferedImage in, int zoom) {
		BufferedImage out = new BufferedImage(in.getWidth(), in.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform trans = new AffineTransform();
		trans.scale(zoom, zoom);
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BICUBIC);
		out = op.filter(in, null);
		return out;
	}

	public static BufferedImage rotate(BufferedImage img, double degree) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		AffineTransform trans = new AffineTransform();
		trans.rotate(degree, img.getWidth() / 2, img.getHeight() / 2);
		AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
		out = op.filter(img, out);
		return out;
	}

	public static BufferedImage add(BufferedImage img1, BufferedImage img2) {

		if (img1.getHeight() != img2.getHeight() || img1.getWidth() != img2.getWidth()) {
			System.out.println("Error not same size!");
			return null;
		}
		int width = img1.getWidth();
		int height = img1.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		byte[] outData = ImageUtils.getData(out);
		byte[] img1Data = ImageUtils.getData(img1);
		byte[] img2Data = ImageUtils.getData(img2);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				int col = img1Data[x + y * width];
				int col2 = img2Data[x + y * width];
				int redO = (col >> 16) & 0xFF;
				int greenO = (col >> 8) & 0xFF;
				int blueO = col & 0xFF;

				int rnew = ((col2 >> 16) & 0xFF) + redO;
				int gnew = ((col2 >> 8) & 0xFF) + greenO;
				int bnew = (col2 & 0xFF) + blueO;
				if (rnew > 255)
					rnew = 255;
				if (gnew > 255)
					gnew = 255;
				if (bnew > 255)
					bnew = 255;
				outData[x + (y * width)] = (byte) (0xFF000000 | (rnew << 16) | (gnew << 8) | (bnew));
			}
		return out;
	}

	public static BufferedImage resize(Image originalImage, int scaledWidth, int scaledHeight, boolean preserveAlpha) {
		// System.out.println("resizing...");
		int imageType = preserveAlpha ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB;
		BufferedImage scaledBI = new BufferedImage(scaledWidth, scaledHeight, imageType);
		Graphics2D g = scaledBI.createGraphics();
		if (preserveAlpha) {
			g.setComposite(AlphaComposite.Src);
		}
		g.drawImage(originalImage, 0, 0, scaledWidth, scaledHeight, null);
		g.dispose();
		return scaledBI;
	}

	/**
	 * Not really image subtraction
	 * 
	 * @param img1
	 * @param img2
	 * @return
	 */
	public static BufferedImage subtract(BufferedImage img1, BufferedImage img2) {

		if (img1.getHeight() != img2.getHeight() || img1.getWidth() != img2.getWidth()) {
			System.out.println("Error not same size!");
			return null;
		}
		int width = img1.getWidth();
		int height = img1.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				Color col = new Color(img1.getRGB(x, y));
				Color col2 = new Color(img2.getRGB(x, y));
				int redO = col.getRed();
				int greenO = col.getGreen();
				int blueO = col.getBlue();

				double rnew = redO / 255d;
				double gnew = greenO / 255d;
				double bnew = blueO / 255d;

				out.setRGB(x, y, new Color((int) (col2.getRed() - rnew), (int) (col2.getGreen() - gnew),
						(int) (col2.getBlue() - bnew)).getRGB());
			}
		return out;
	}

	public static BufferedImage divide(BufferedImage img1, BufferedImage img2) {

		if (img1.getHeight() != img2.getHeight() || img1.getWidth() != img2.getWidth()) {
			System.out.println("Error not same size!");
			return null;
		}
		int width = img1.getWidth();
		int height = img1.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		byte[] outData = ImageUtils.getData(out);
		byte[] img1Data = ImageUtils.getData(img1);
		byte[] img2Data = ImageUtils.getData(img2);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				Color col = new Color(img1Data[x + (y * width)]);
				Color col2 = new Color(img2Data[x + (y * width)]);
				int redO = col.getRed();
				int greenO = col.getGreen();
				int blueO = col.getBlue();

				double rnew = redO / 255d;
				double gnew = greenO / 255d;
				double bnew = blueO / 255d;
				int r = (int) (col2.getRed() / rnew);
				int g = (int) (col2.getGreen() / gnew);
				int b = (int) (col2.getBlue() / bnew);
				if (r > 255)
					r = 255;
				if (r < 0)
					r = 0;
				if (g > 255)
					g = 255;
				if (g < 0)
					g = 0;
				if (b > 255)
					b = 255;
				if (b < 0)
					b = 0;
				outData[x + (y * width)] = (byte) getRGB(r, g, b);
			}
		return out;
	}

	public static BufferedImage multiply(BufferedImage img1, BufferedImage img2) {

		if (img1.getHeight() != img2.getHeight() || img1.getWidth() != img2.getWidth()) {
			System.out.println("Error not same size!");
			return null;
		}
		int width = img1.getWidth();
		int height = img1.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		byte[] outData = ImageUtils.getData(out);
		byte[] img1Data = ImageUtils.getData(img1);
		byte[] img2Data = ImageUtils.getData(img2);
		for (int y = 0; y < height; y++)
			for (int x = 0; x < width; x++) {
				Color col = new Color(img1Data[x + y * width]);
				Color col2 = new Color(img2Data[x + y * width]);
				int redO = col.getRed();
				int greenO = col.getGreen();
				int blueO = col.getBlue();

				double rnew = redO / 255d;
				double gnew = greenO / 255d;
				double bnew = blueO / 255d;

				outData[x + y * width] = (byte) getRGB((int) (rnew * col2.getRed()), (int) (gnew * col2.getGreen()),
						(int) (bnew * col2.getBlue()));
			}
		return out;
	}

	public static BufferedImage negate(BufferedImage img) {

		int wi = img.getWidth();
		int he = img.getHeight();
		BufferedImage out = new BufferedImage(wi, he, BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < he; y++)
			for (int x = 0; x < wi; x++) {
				Color col = new Color(img.getRGB(x, y));
				int originalRed = Math.abs(255 - col.getRed());
				int originalGreen = Math.abs(255 - col.getGreen());
				int originalBlue = Math.abs(255 - col.getBlue());
				out.setRGB(x, y, new Color(originalRed, originalGreen, originalBlue).getRGB());
			}
		return out;
	}

	public static BufferedImage Sobel(BufferedImage inputImg) {
		BufferedImage outputImg = new BufferedImage(inputImg.getWidth(), inputImg.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		for (int i = 1; i < inputImg.getWidth() - 1; i++) {
			for (int j = 1; j < inputImg.getHeight() - 1; j++) {
				matrix[0][0] = new Color(inputImg.getRGB(i - 1, j - 1)).getRed();
				matrix[0][1] = new Color(inputImg.getRGB(i - 1, j)).getRed();
				matrix[0][2] = new Color(inputImg.getRGB(i - 1, j + 1)).getRed();
				matrix[1][0] = new Color(inputImg.getRGB(i, j - 1)).getRed();
				matrix[1][2] = new Color(inputImg.getRGB(i, j + 1)).getRed();
				matrix[2][0] = new Color(inputImg.getRGB(i + 1, j - 1)).getRed();
				matrix[2][1] = new Color(inputImg.getRGB(i + 1, j)).getRed();
				matrix[2][2] = new Color(inputImg.getRGB(i + 1, j + 1)).getRed();

				int edge = (int) convolution(matrix);
				outputImg.setRGB(i, j, (edge << 16 | edge << 8 | edge));
			}
		}

		return outputImg;

	}

	private static double convolution(int[][] matrix) {

		int gy = (int) ((matrix[0][0] * -1) + (matrix[0][1] * -2) + (matrix[0][2] * -1) + (matrix[2][0])
				+ (matrix[2][1] * 2) + (matrix[2][2] * 1));
		int gx = (matrix[0][0]) + (matrix[0][2] * -1) + (matrix[1][0] * 2) + (matrix[1][2] * -2) + (matrix[2][0])
				+ (matrix[2][2] * -1);
		return Math.sqrt(Math.pow(gy, 2) + Math.pow(gx, 2));

	}

	public void setImage(String path) throws IOException {
		imagePath = path;
		image = ImageIO.read(new File(path));
		initPixels();
	}

	private void initPixels() {
		pixels = new Integer[image.getWidth()][image.getHeight()];
	}

	public static Integer[][] getPixels(BufferedImage img) {
		Integer[][] tmp = new Integer[img.getWidth()][img.getHeight()];
		for (int i = 0; i < img.getWidth(); i++)
			for (int j = 0; j < img.getHeight(); j++)
				tmp[i][j] = img.getRGB(i, j);
		return tmp;
	}

	public void retrivePixels() {
		CGS();
		for (int i = 0; i < gImage.getWidth(); i++)
			for (int j = 0; j < gImage.getHeight(); j++)
				pixels[i][j] = gImage.getRGB(i, j);
	}

	public static int[] toSingleArray(int[][] data) {
		List<Integer> list = new ArrayList<Integer>();
		Integer[] intList = new Integer[data.length];
		for (int i = 0; i < data.length; i++) {
			for (int j = 0; j < data[i].length; j++) {
				list.add(data[i][j]);
			}
		}

		return Arrays.stream(list.toArray(intList)).mapToInt(Integer::intValue).toArray();
	}

	public static BufferedImage Smoothen(BufferedImage img) {
		BufferedImage out = img;
		int size = img.getWidth();
		double[][] heightmap = retrivePixelsD(img, size, size);
		for (int i = 1; i < size - 1; ++i) {
			for (int j = 1; j < size - 1; ++j) {
				double total = 0.0;

				for (int u = -1; u <= 1; u++) {
					for (int v = -1; v <= 1; v++) {
						total += heightmap[i + u][j + v];
					}
				}

				heightmap[i][j] = total / 20;
			}

		}
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				heightmap[x][y] = (heightmap[x][y] - 1) / (1 - -1);
			}
		}
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				out.setRGB(x, y, (int) heightmap[x][y]);
			}
		return out;
	}

	public static BufferedImage Normalize(BufferedImage img) {
		BufferedImage out = img;
		int size = img.getWidth();
		int sizey = img.getHeight();
		int[][] heightmap = retrivePixels(img, size, sizey);
		for (int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				heightmap[x][y] = (heightmap[x][y] - 1) / (1 - -1);
			}
		}
		for (int y = 0; y < size; y++)
			for (int x = 0; x < size; x++) {
				out.setRGB(x, y, heightmap[x][y]);
			}
		return out;

	}

	public static int[][] retrivePixels(BufferedImage img, int w, int h) {
		int[][] pix = new int[w][h];
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				pix[i][j] = img.getRGB(i, j);
		return pix;
	}

	public static double[][] retrivePixelsD(BufferedImage img, int w, int h) {
		double[][] pix = new double[w][h];
		for (int i = 0; i < w; i++)
			for (int j = 0; j < h; j++)
				pix[i][j] = img.getRGB(i, j);
		return pix;
	}

	public Integer[][] getPixels() {
		return this.pixels;
	}

	public static BufferedImage blur2(BufferedImage tbi, int radius) {

		int size = radius * 2 + 1;
		float weight = 1.0f / (size * size);
		float[] data = new float[size * size];

		for (int i = 0; i < data.length; i++) {
			data[i] = weight;
		}

		Kernel kernel = new Kernel(size, size, data);
		ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
		// tbi is BufferedImage
		BufferedImage i = op.filter(tbi, null);

		return i;
	}

	public static BufferedImage CGS(BufferedImage ih) {
		ImageFilter filter = new GrayFilter(true, 50);
		ImageProducer producer = new FilteredImageSource(ih.getSource(), filter);
		Image mage = Toolkit.getDefaultToolkit().createImage(producer);

		return toBufferedImage(mage);

	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}

		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();

		// Return the buffered image
		return bimage;
	}

	private void CGS() {
		int x = image.getHeight();
		int y = image.getWidth();

		gImage = new BufferedImage(x, y, BufferedImage.TYPE_BYTE_GRAY);

		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				int rgb = image.getRGB(i, j);
				int r = (rgb >> 16) & 0xFF;
				int g = (rgb >> 8) & 0xFF;
				int b = (rgb & 0xFF);

				int grayLevel = (r + g + b) / 3;
				int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
				image.setRGB(i, j, gray);
			}
		}
	}

	public static byte[] getData(BufferedImage img) {
		byte[] arr = new byte[img.getWidth() * img.getHeight()];
		for (int x = 0; x < img.getWidth(); x++)
			for (int y = 0; y < img.getHeight(); y++) {
				arr[y * img.getWidth() + x] = (byte) img.getRGB(x, y);
			}
		return arr;
	}

	/**
	 * WARNING UNCHECKED!!! There will be no error if any color is greater than 255,
	 * but the result will be garbage
	 * 
	 * @param r
	 * @param g
	 * @param b
	 * @return
	 */
	public static int getRGB(int r, int g, int b) {
		return 0xFF000000 | (r >> 16) | (g >> 8) | b;
	}
}
