package at.crimsonbit.nodesystem.terrain;

import java.awt.image.BufferedImage;

public interface IMorph {
	public abstract BufferedImage Erode(BufferedImage image, int n);

	public abstract BufferedImage Dilate(BufferedImage image, int n);

	public default BufferedImage perturbImg(BufferedImage image, double f, double d) {
		return Morph.Perturb(image, f, d);
	}
}
