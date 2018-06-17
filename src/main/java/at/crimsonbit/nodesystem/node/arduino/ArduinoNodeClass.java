package at.crimsonbit.nodesystem.node.arduino;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public class ArduinoNodeClass extends GNode {

	public ArduinoNodeClass() {
		super();
	}

	public ArduinoNodeClass(String name, int id, boolean draw, GNodeGraph graph) {
		super(name, id, draw, graph);
		addPopUpItem(5, "Initialize"); // Adds a custom pop-up menu item.
	}

	public ArduinoNodeClass(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);

		addPopUpItem(5, "Initialize");
	}

	@Override
	public void consumeCustomMessage(int id) {
		if (id == 5) {
			getAbstractNode().get("arduino");
			getAbstractNode().get("forward");
			getAbstractNode().get("value");
		}
	}

}
