package at.crimsonbit.nodesystem.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.GNodeConnection;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.node.types.BaseType;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
/**
 * 
 * @author NeonArtworks
 *
 */
public class GNodeMaster {

	private GNode graphParent;
	private NodeMaster nodeMaster;
	private List<GNode> allCells;
	private List<GNode> addedCells;
	private List<GNode> removedCells;

	private List<GNodeConnection> allEdges;
	private List<GNodeConnection> addedEdges;
	private List<GNodeConnection> removedEdges;

	private Map<String, GNode> cellMap; // <id,cell>

	private GPort outPort;
	private GPort inPort;

	public void setFirstPort(GPort port) {
		this.outPort = port;
	}

	public void setSecondPort(GPort port) {
		this.inPort = port;
	}

	public void connectPorts() {
		if (this.outPort != null && this.inPort != null) {
			if (this.outPort.isInput()) {
				this.outPort = null;
				this.inPort = null;
				return;
			}
			if (!this.inPort.isInput()) {
				this.inPort = null;
				this.outPort = null;
				return;
			}
			if (this.outPort != null && this.inPort != null) {
				addConnection(this.outPort, this.inPort);
				this.outPort = null;
				this.inPort = null;
			}
		}
	}

	public void removeConnection(GPort port) {
		for (GNodeConnection con : getAllEdges()) {
			if (con.getSourcePort() == port || con.getTargetPort() == port) {
				try {
					getNodeMaster().removeConnection(con.getTarget().getNode(), con.getTargetPort().getStringID());
				} catch (NoSuchNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRemovedEdges().add(con);
			}
		}
	}

	public GNodeMaster() {
		this.nodeMaster = new NodeMaster();
		this.nodeMaster.registerNodes("at.crimsonbit.nodesystem.node.nodes");
		this.graphParent = new GNode("_ROOT_", false);

		// clear model, create lists
		clear();
	}

	public void registerNodes(String packag) {
		this.nodeMaster.registerNodes(packag);
	}

	public void clear() {

		allCells = new ArrayList<GNode>();
		addedCells = new ArrayList<GNode>();
		removedCells = new ArrayList<GNode>();

		allEdges = new ArrayList<GNodeConnection>();
		addedEdges = new ArrayList<GNodeConnection>();
		removedEdges = new ArrayList<GNodeConnection>();

		cellMap = new HashMap<String, GNode>(); // <id,cell>

	}

	public NodeMaster getNodeMaster() {
		return nodeMaster;
	}

	public void clearAddedLists() {
		addedCells.clear();
		addedEdges.clear();
	}
	
	public void removeNode(GNode node) {
		for (GNodeConnection c : getAllEdges()) {
			if (c.getSource() == node || c.getTarget() == node) {
				try {
					getNodeMaster().removeConnection(c.getTarget().getNode(), c.getTargetPort().getStringID());
				} catch (NoSuchNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				removedEdges.add(c);
			}
		}
		removedCells.add(node);
		cellMap.remove(node.getName());
	}

	public List<GNode> getAddedCells() {
		return addedCells;
	}

	public List<GNode> getRemovedCells() {
		return removedCells;
	}

	public List<GNode> getAllCells() {
		return allCells;
	}

	public List<GNodeConnection> getAddedEdges() {
		return addedEdges;
	}

	public List<GNodeConnection> getRemovedEdges() {
		return removedEdges;
	}

	public List<GNodeConnection> getAllEdges() {
		return allEdges;
	}

	public void addNode(String id, INodeType type, boolean draw, GNodeGraph graph) {
		addNode(new GNode(id, type, draw, graph));

	}

	public void addNode(String id, BaseType type, boolean draw, GNodeGraph graph) {
		addNode(new GNode(id, type, draw, graph));
	}

	public void addNode(GNode cell) {
		addedCells.add(cell);
		cellMap.put(cell.getName(), cell);

	}

	/*
	 * public void addConnection(String sourceId, String targetId) {
	 * 
	 * GNode sourceCell = cellMap.get(sourceId); GNode targetCell =
	 * cellMap.get(targetId);
	 * 
	 * GNodeConnection edge = new GNodeConnection(sourceCell, targetCell);
	 * 
	 * 
	 */

	public void addConnection(GPort node1, GPort node2) {

		if (node1.getNode() == node2.getNode())
			return;

		GNodeConnection con = new GNodeConnection(node1, node2);
		try {
			if (node1.isInput()) {
				getNodeMaster().setConnection(node1.getNode().getNode(), node1.getStringID(), node2.getNode().getNode(),
						node2.getStringID());
			} else if (node2.isInput()) {
				getNodeMaster().setConnection(node2.getNode().getNode(), node2.getStringID(), node1.getNode().getNode(),
						node1.getStringID());
			}
		} catch (NoSuchNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		addedEdges.add(con);
	}

	/**
	 * Attach all cells which don't have a parent to graphParent
	 * 
	 * @param cellList
	 */
	public void attachOrphansToGraphParent(List<GNode> cellList) {

		for (GNode cell : cellList) {
			if (cell.getCellParents().size() == 0) {
				graphParent.addCellChild(cell);
			}
		}

	}

	/**
	 * Remove the graphParent reference if it is set
	 * 
	 * @param cellList
	 */
	public void disconnectFromGraphParent(List<GNode> cellList) {

		for (GNode cell : cellList) {
			graphParent.removeCellChild(cell);
		}
	}

	public void merge() {

		// cells
		allCells.addAll(addedCells);
		allCells.removeAll(removedCells);

		addedCells.clear();
		removedCells.clear();

		// edges
		allEdges.addAll(addedEdges);
		allEdges.removeAll(removedEdges);

		addedEdges.clear();
		removedEdges.clear();

	}

}
