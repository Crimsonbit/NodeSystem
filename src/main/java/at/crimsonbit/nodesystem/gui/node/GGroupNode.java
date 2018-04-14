package at.crimsonbit.nodesystem.gui.node;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.toast.Toast;
import at.crimsonbit.nodesystem.gui.toast.ToastTime;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public class GGroupNode extends GNode {

	public GGroupNode() {
		super();
	}

	public GGroupNode(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);

		getPopUpDialog().addItem(8, "Make Toast");
		updatePopUpDialog();
	}

	@Override
	public void consumeCustomMessage(int id) {
		if (id == 8) {
			Toast.makeToast("Hier könnte Ihre Werbugn stehen, wussten Sie das eigenlich? :D", ToastTime.TIME_SHORT);
		}
	}

}
