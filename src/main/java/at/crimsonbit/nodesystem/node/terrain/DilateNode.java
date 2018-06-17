package at.crimsonbit.nodesystem.node.terrain;

import java.awt.image.BufferedImage;

import at.crimsonbit.nodesystem.node.types.Terrain;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.NodeInput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeOutput;
import at.crimsonbit.nodesystem.nodebackend.api.NodeType;
import at.crimsonbit.nodesystem.terrain.OpenCLMorph;
import at.crimsonbit.nodesystem.terrain.SingletonCLMorph;

public class DilateNode extends AbstractNode {

	@NodeType
	private static final Terrain type = Terrain.DIALTE;
	private OpenCLMorph clm = SingletonCLMorph.getInstance();

	@NodeInput
	BufferedImage input;

	@NodeInput
	int amount;

	@NodeOutput("doDilate")
	BufferedImage output;

	public void doDilate() {
		if (input != null && amount > 0) {
			try {
				output = clm.Dilate(input, amount);
			} finally {
				//clm.close();
			}
		}
	}

}
