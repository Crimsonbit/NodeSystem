package at.crimsonbit.nodesystem.gui;

/**
 * 
 * The NodeSystem main class.
 * 
 * @author NeonArtworks
 *
 */

public class GNodeSystem {

	private GNodeView nodeView;

	public GNodeSystem(boolean addPane) {
		this.nodeView = new GNodeView(addPane);
	}
	
	public GNodeView getGUI() {
		return this.nodeView;
	}

}
