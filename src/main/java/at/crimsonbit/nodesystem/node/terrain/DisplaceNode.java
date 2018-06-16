package at.crimsonbit.nodesystem.node.terrain;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Terrain;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.terrain.Displacement;
import at.crimsonbit.nodesystem.util.BaseContainer;

public class DisplaceNode extends AbstractNode {

	@NodeType
	private static final Terrain type = Terrain.DISPLACEMENT;
	private Displacement dis = new Displacement();
	private long oldSeed;

	@NodeOutput("generateDisplacement")
	BufferedImage output;

	@NodeInput
	long seed;

	@NodeInput
	int width;

	@NodeInput
	int height;

	public void generateDisplacement() {
		if (width != 0 && height != 0 && !(oldSeed == seed)) {
			//BaseContainer cont = dis.getSettings();
			//cont.addEntry("DISP_SEED", seed);
			//dis.setSettings(cont);
			output = dis.createDisplacement(width, height, "");
			oldSeed = seed;
		}
	}

}
