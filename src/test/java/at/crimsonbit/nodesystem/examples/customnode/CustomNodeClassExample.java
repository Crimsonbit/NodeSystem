package at.crimsonbit.nodesystem.examples.customnode;

import java.util.logging.Level;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.animation.Animator;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.widget.toast.Toast;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastPosition;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastTime;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import javafx.stage.Stage;

/**
 * 
 * Example of how to use your custom node together with a custom node class.
 * Please note, that if you don't want to react to custom messages your don't
 * need to use a custom class. By Default every node uses the standard GNode
 * class.
 * 
 * @author Florian Wagner
 * 
 */
public class CustomNodeClassExample extends GNode {

	public CustomNodeClassExample() {
		super();
	}

	// This constructor HAS TO BE in your custom node class.
	public CustomNodeClassExample(String name, int id, boolean draw, GNodeGraph graph) {
		super(name, id, draw, graph);
		addPopUpItem(5, "Make Toast"); // Adds a custom pop-up menu item.
		addPopUpItem(6, "Animate"); // Adds a custom pop-up menu item.
		addPopUpItem(7, "Append Log"); // Adds a custom pop-up menu item.
	}

	public CustomNodeClassExample(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		super(name, type, draw, graph, x, y);
		addPopUpItem(5, "Make Toast"); // Adds a custom pop-up menu item.
		addPopUpItem(6, "Animate"); // Adds a custom pop-up menu item.
		addPopUpItem(7, "Append Log"); // Adds a custom pop-up menu item.
	}

	/**
	 * Every pop-up message id that is above getInternalIDCounter() will be consumed
	 * by the method below. You can react to your custom id's here.
	 */
	@Override
	public void consumeCustomMessage(int id) {
		if (id == 5) {
			Toast.makeToast((Stage) getScene().getWindow(), "Sample Text!", ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
		}
		if (id == 6) {
			Animator.animateProperty(opacityProperty(), 500, 200, 200, 0, 1);
		}
		if (id == 7) {
			// the log function is located in the nodegraph.
			getNodeGraph().log(Level.WARNING, "This is a custom log warning!");
		}
	}

}
