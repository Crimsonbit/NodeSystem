package at.crimsonbit.nodesystem.node.terrain;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Terrain;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
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
			try (MultiThreadMorph clm = new MultiThreadMorph(ThreadPool.getThreadPool().getPool(),
					ThreadPool.getThreadPool().getThreads())) {
				output = clm.Erode(input, amount);
			}
		}
	}

}