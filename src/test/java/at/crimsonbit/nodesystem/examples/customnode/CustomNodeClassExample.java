package at.crimsonbit.nodesystem.examples.customnode;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.animation.Animator;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.toast.Toast;
import at.crimsonbit.nodesystem.gui.toast.ToastTime;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

public class CustomNodeClassExample extends GNode {

	public CustomNodeClassExample() {
		super();
	}
	
	public CustomNodeClassExample(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		// int ppc = getInternalIDCounter(); //Tells you the latest popup-menu id used
		// internally
		addPopUpItem(8, "Make Toast"); // Adds a custom pop-up menu item.
		addPopUpItem(9, "Animate");
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == 8) {
			Toast.makeToast("Sample Text!", ToastTime.TIME_SHORT);
		}
		if (id == 9) {
			Animator.animateProperty(opacityProperty(), 200, 400, 200, 0, 1);
		}
	}

}
