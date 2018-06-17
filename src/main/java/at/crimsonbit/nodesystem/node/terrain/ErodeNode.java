package at.crimsonbit.nodesystem.node.terrain;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Terrain;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.terrain.MultiThreadMorph;
import at.crimsonbit.nodesystem.terrain.OpenCLMorph;
import at.crimsonbit.nodesystem.terrain.SingletonCLMorph;

public class ErodeNode extends AbstractNode {

	@NodeType
	private static final Terrain type = Terrain.ERODE;
	private OpenCLMorph clm = SingletonCLMorph.getInstance();

	@NodeInput
	BufferedImage input;

	@NodeInput
	int amount;

	@NodeOutput("doErode")
	BufferedImage output;

	public void doErode() {
		if (input != null && amount > 0) {
			try {
				output = clm.Erode(input, amount);
			} finally {
				//clm.close();
			}
		}
	}

}