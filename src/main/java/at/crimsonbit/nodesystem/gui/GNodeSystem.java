package at.crimsonbit.nodesystem.gui;

/**
 * <h1>NodeSystem</h1>
 * <p>
 * The NodeSystem class is the main class of the NodeSystem library. All you
 * need to do to get everything up and running, is, to instantiate this class
 * and call getNodeview() the retrieve {@link GNodeView} *
 * </p>
 * 
 * @author Florian Wagner
 *
 */

public class GNodeSystem {

	private GNodeView nodeView;

	public GNodeSystem() {
		nodeView = new GNodeView(false);
	}

	public void attachSettingsPane(boolean addPane) {
		this.nodeView = new GNodeView(addPane);
	}

	public GNodeView getNodeView() {
		return this.nodeView;
	}

}
