package at.crimsonbit.nodesystem.terrain;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.IntConsumer;

import at.crimsonbit.nodesystem.util.ImageUtils;
import at.crimsonbit.nodesystem.util.OpenSimplexNoise;

public class MultiThreadMorph implements IMorph, AutoCloseable{
	private final ExecutorService pool;
	private final int poolSize;

	/**
	 * Any created MultiThreadMorph should be shut down using
	 * {@link MultiThreadMorph#shutdown()} to prevent Memory leaks
	 */
	public MultiThreadMorph() {
		this(Runtime.getRuntime().availableProcessors());
	}

	/**
	 * Any created MultiThreadMorph should be shut down using
	 * {@link MultiThreadMorph#shutdown()} to prevent Memory leaks
	 */
	public MultiThreadMorph(int poolSize) {
		this.poolSize = poolSize;
		pool = Executors.newFixedThreadPool(poolSize);
	}

	/**
	 * @see ExecutorService#shutdown()
	 */
	public void shutdown() {
		pool.shutdown();
	}

	/**
	 * If called after {@link MultiThreadMorph#shutdown()} it waits until the
	 * internal ExecutorService is fully shut down
	 * 
	 * @param timeout
	 * @param unit
	 * @throws InterruptedException
	 * @see ExecutorService#shutdown()
	 */
	public void awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		pool.awaitTermination(timeout, unit);
	}

	public BufferedImage Erode(BufferedImage img, int n) {
		/**
		 * Dimension of the image img.
		 */
		int width = img.getWidth();
		int height = img.getTileHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

		// perform erosion
		// buff = new int[100];
		int[] imgData = ImageUtils.getData(img);
		for (int j = 0; j < n; j++) {
			int[] outRGBBuffer = ((DataBufferInt) (out.getRaster().getDataBuffer())).getData();
			CountDownLatch counter = new CountDownLatch(poolSize);
			int[] currys = new int[poolSize + 1];
			currys[0] = 0;
			for (int i = 1; i < currys.length - 1; i++) {
				currys[i] = currys[i - 1] + height / poolSize;
			}
			currys[currys.length - 1] = height;
			for (int t = 0; t < poolSize; t++) {
				Runnable run = new LoopingThread((ind) -> {
					for (int y = currys[ind]; y < currys[ind + 1]; y++) {
						for (int x = 0; x < width; x++) {
							outRGBBuffer[x + (y * width)] = Morph.findMinInRange(imgData, width, height, y, x);
						}
					}
					counter.countDown();
				}, t);
				pool.submit(run);
			}
			try {
				counter.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} /**
				 * Save the erosion value in image img.
				 */

			System.arraycopy(outRGBBuffer, 0, imgData, 0, img.getHeight() * img.getWidth());
		}
		return out;
	}

	public BufferedImage Dilate(BufferedImage img, int n) {
		/**
		 * Dimension of the image img.
		 */
		int width = img.getWidth();
		int height = img.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		// output of dilation
		int[] outRGBBuffer = ((DataBufferInt) (out.getRaster().getDataBuffer())).getData();
		int[] imgData = ImageUtils.getData(img);
		for (int j = 0; j < n; j++) {
			CountDownLatch counter = new CountDownLatch(poolSize);
			int[] currys = new int[poolSize + 1];
			currys[0] = 0;
			for (int i = 1; i < currys.length - 1; i++) {
				currys[i] = currys[i - 1] + height / poolSize;
			}
			currys[currys.length - 1] = height;

			for (int t = 0; t < poolSize; t++) {
				Runnable run = new LoopingThread((ind) -> {
					for (int y = currys[ind]; y < currys[ind + 1]; y++) {
						for (int x = 0; x < width; x++) {
							outRGBBuffer[x + (y * width)] = Morph.findMaxInRange(imgData, width, height, y, x);
						}
					}
					counter.countDown();
				}, t);
				pool.submit(run);
			}
			try {
				counter.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			/**
			 * Save the erosion value in image img.
			 */
			System.arraycopy(outRGBBuffer, 0, imgData, 0, img.getHeight() * img.getWidth());
		}
		return out;
	}

	public BufferedImage PerturbImg(BufferedImage img, double f, double d) {
		BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());
		int size = img.getWidth();
		OpenSimplexNoise noise = new OpenSimplexNoise();
		int[] heightmap = ImageUtils.getData(img);
		int[] outRGBBuffer = ImageUtils.getData(out);

		CountDownLatch counter = new CountDownLatch(poolSize);
		int[] currys = new int[poolSize + 1];
		currys[0] = 0;
		for (int i = 1; i < currys.length - 1; i++) {
			currys[i] = currys[i - 1] + (size / poolSize);
		}
		currys[currys.length - 1] = size;

		for (int t = 0; t < poolSize; t++) {
			Runnable run = new LoopingThread((ind) -> {
				int u, v;
				for (int i = 0; i < size; ++i) {
					for (int j = currys[ind]; j < currys[ind + 1]; ++j) {
						double x = f * i / ((double) size);
						double y = f * j / ((double) size);
						u = i + (int) (noise.eval(x, y, 0) * d);
						v = j + (int) (noise.eval(x, y, 1) * d);

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

						outRGBBuffer[i + (j * size)] = heightmap[u + (v * size)];
					}
				}
				counter.countDown();
			}, t);
			pool.submit(run);
		}
		try {
			counter.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return out;
	}

	private static class LoopingThread implements Runnable {
		private IntConsumer function;
		private int index;

		public LoopingThread(IntConsumer function, int index) {
			this.index = index;
			this.function = function;
		}

		@Override
		public void run() {
			function.accept(index);
		}

	}

	@Override
	public void close() {
		shutdown();
	}
}
