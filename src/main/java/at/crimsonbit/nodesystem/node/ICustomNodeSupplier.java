package at.crimsonbit.nodesystem.node;

import at.crimsonbit.nodesystem.gui.node.GNode;

public interface ICustomNodeSupplier {
	Class<? extends GNode> getCustomNodeClass();
}
