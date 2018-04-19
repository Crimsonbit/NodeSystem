package at.crimsonbit.nodesystem.node.base;

import java.io.File;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public class PathNodeClass extends GNode {

	private int ppc;

	public PathNodeClass() {
		super();
	}

	public PathNodeClass(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		ppc = getInternalIDCounter() + 1;
		addPopUpItem(ppc, "set Path"); // Adds a custom pop-up menu item.
	}

	public PathNodeClass(String name, int id, boolean draw, GNodeGraph graph) {
		super(name, id, draw, graph);
		ppc = getInternalIDCounter() + 1;
		addPopUpItem(ppc, "set Path"); // Adds a custom pop-up menu item.
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == ppc) {
			setPath();
			redraw();
			getNodeGraph().update();
		}
	}

	@Override
	public String toString() {
		String str = name + ", connections=" + connections + ", type=" + type + ", inPortCount=" + inPortCount
				+ ", outPortcount=" + outPortcount + ", ppc=" + ppc;
		if (this.getAbstractNode() != null)
			return str + "\npath: " + this.getAbstractNode().get("path");
		else
			return str;
	}

	public String getPath() {
		if (getNodeType().equals(Base.PATH)) {
			if (this.getAbstractNode() != null)
				return (String) this.getAbstractNode().get("path");
		}

		return new String("null");
	}

	public void setPath() {
		if (getNodeType().equals(Base.PATH)) {
			File f = fileChooser.showOpenDialog(getParent().getScene().getWindow());
			if (f != null)
				this.getAbstractNode().set("path", f.getPath());
		}

	}
}
