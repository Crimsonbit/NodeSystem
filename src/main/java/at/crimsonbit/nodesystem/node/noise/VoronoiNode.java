package at.crimsonbit.nodesystem.node.noise;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Noise;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.terrain.Voronoi;

public class VoronoiNode extends AbstractNode {

	@NodeType
	private static final Noise type = Noise.VORONOI;

	private long oldSeed;

	@NodeOutput("genVoro")
	BufferedImage output;

	@NodeInput
	int cells;

	@NodeInput
	int width;

	@NodeInput
	int height;

	public void genVoro() {
		if (width != 0 && height != 0 && !(oldSeed == cells)) {
			Voronoi dis = new Voronoi(width, height, (int) cells);
			// BaseContainer cont = dis.getSettings();
			// cont.addEntry("DISP_SEED", seed);
			// dis.setSettings(cont);
			output = dis.getVoronoi();
			oldSeed = cells;
		}
	}
}