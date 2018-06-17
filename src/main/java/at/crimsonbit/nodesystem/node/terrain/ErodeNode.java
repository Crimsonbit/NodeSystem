package at.crimsonbit.nodesystem.node.terrain;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Terrain;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.terrain.IMorph;
import at.crimsonbit.nodesystem.terrain.MorphFactory;
import at.crimsonbit.nodesystem.terrain.MultiThreadMorph;
import at.crimsonbit.nodesystem.util.ThreadPool;

public class ErodeNode extends AbstractNode {

	@NodeType
	private static final Terrain type = Terrain.ERODE;

	@NodeInput
	BufferedImage input;

	@NodeInput
	int amount;

	@NodeOutput("doErode")
	BufferedImage output;

	public void doErode() {
		if (input != null && amount > 0) {
			try (IMorph morph = MorphFactory.getMorph()) {
				System.out.println(morph.getClass());
				output = morph.Erode(input, amount);
			}
		}
	}

}