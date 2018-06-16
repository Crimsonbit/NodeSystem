package at.crimsonbit.nodesystem.node.noise;

import java.awt.Color;
import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Noise;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.util.OpenSimplexNoise;

public class SimplexNode extends AbstractNode {

	@NodeType
	private static final Noise type = Noise.SIMPLEX;

	private long oldSeed;

	@NodeOutput("genSimplex")
	BufferedImage output;

	@NodeInput
	int seed;

	@NodeInput
	int width;

	@NodeInput
	int height;

	public void genSimplex() {
		if (width != 0 && height != 0 && !(oldSeed == seed)) {
			output = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			OpenSimplexNoise noise = new OpenSimplexNoise(seed);
			// BaseContainer cont = dis.getSettings();
			// cont.addEntry("DISP_SEED", seed);
			// dis.setSettings(cont);
			for (int x = 0; x < width; x++)
				for (int y = 0; y < height; y++) {
					double z = noise.eval(x, y);
					Color c = new Color((int) z, (int) z, (int) z);
					output.setRGB(x, y, c.getRGB());
				}

			oldSeed = seed;
		}
	}
}
