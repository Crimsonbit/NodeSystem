package at.crimsonbit.nodesystem.node.base;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public class OutputNodeClass extends GNode {

	private int ppc;

	public OutputNodeClass() {
		super();
	}

	public OutputNodeClass(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		ppc = getInternalIDCounter() + 1;
		addPopUpItem(ppc, "get Output"); // Adds a custom pop-up menu item.
	}

	public OutputNodeClass(String name, int id, boolean draw, GNodeGraph graph) {
		super(name, id, draw, graph);
		ppc = getInternalIDCounter() + 1;
		addPopUpItem(ppc, "get Output"); // Adds a custom pop-up menu item.
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == ppc) {
			setOutput();
			redraw();
			getNodeGraph().update();
		}
	}

	@Override
	public String toString() {
		String str = name + ", connections=" + connections + ", type=" + type + ", inPortCount=" + inPortCount
				+ ", outPortcount=" + outPortcount + ", ppc=" + ppc;
		if (this.getAbstractNode() != null)
			return str + "\noutput: " + this.getAbstractNode().get("output");
		else
			return str;
	}

	public Object getOutput() {
		if (getNodeType().equals(Base.OUTPUT)) {
			if (this.getAbstractNode() != null)
				return this.getAbstractNode().get("output");
		}
		return new String("null");
	}

	public void setOutput() {
		if (getNodeType().equals(Base.OUTPUT)) {
			setName("Output - " + String.valueOf(this.getAbstractNode().get("output")));
			computeNewPortLocations();
			redraw();
		}
	}
}
