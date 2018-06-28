package at.crimsonbit.nodesystem.node.types;

import at.crimsonbit.nodesystem.gui.node.GNode;

public interface ICustomNodeSupplier {
	Class<? extends GNode> getCustomNodeClass();
}
