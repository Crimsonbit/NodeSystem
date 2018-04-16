package at.crimsonbit.nodesystem.examples.customnode;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.animation.Animator;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.toast.Toast;
import at.crimsonbit.nodesystem.gui.toast.ToastTime;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

/**
 * 
 * Example of how to use your custom node together with a custom node class.
 * Please note, that if you don't want to react to custom messages your don't
 * need to use a custom class. By Default every node uses the standard GNode
 * class.
 * 
 * @author NeonArtworks
 * 
 */
public class CustomNodeClassExample extends GNode {

	public CustomNodeClassExample() {
		super();
	}

	public CustomNodeClassExample(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		// int ppc = getInternalIDCounter(); // Tells you the latest popup-menu id used
		// internally
		addPopUpItem(5, "Make Toast"); // Adds a custom pop-up menu item.
		addPopUpItem(6, "Animate"); // Adds a custom pop-up menu item.
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == 5) {
			Toast.makeToast("Sample Text!\nSample Text!\nSample Text!\nSample Text!\nSample Text!\nSample Text!",
					ToastTime.TIME_SHORT);
		}
		if (id == 6) {
			Animator.animateProperty(opacityProperty(), 500, 200, 200, 0, 1);
		}
	}

}
