package at.crimsonbit.nodesystem.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.GNodeConnection;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.node.types.Base;
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
	private List<GNode> allNodes;
	private List<GNode> addedNodes;
	private List<GNode> removedNodes;

	private List<GNodeConnection> allConnections;
	private List<GNodeConnection> addedConnections;
	private List<GNodeConnection> removedConnections;

	private Map<String, GNode> nodeMap; // <id, node>
	private GNodeGraph graph;
	private GPort outPort;
	private GPort inPort;

	protected void setNodeMaster(NodeMaster nm) {
		this.nodeMaster = nm;
	}

	public GNodeMaster(GNodeGraph graph) {
		this.graph = graph;
		this.nodeMaster = new NodeMaster();
		// this.nodeMaster.registerNodes("at.crimsonbit.nodesystem.node.nodes");
		this.graphParent = new GNode("_ROOT_", false);

		// clear model, create lists

		clear();
	}

	public void setFirstPort(GPort port) {
		this.outPort = port;
	}

	public void setSecondPort(GPort port) {
		this.inPort = port;
	}

	public GNodeGraph getNodeGraph() {
		return this.graph;
	}

	public void removecurConnectPorts() {
		this.outPort = null;
		this.inPort = null;
	}

	public boolean connectPorts() {

		if (this.outPort != null && this.inPort != null) {
			if (this.outPort.isInput()) {
				this.outPort = null;
				this.inPort = null;
				return false;
			}
			if (!this.inPort.isInput()) {
				this.inPort = null;
				this.outPort = null;
				return false;
			}
			if (this.outPort != null && this.inPort != null) {

				if (addConnection(this.outPort, this.inPort)) {
					// System.out.println("INFO!");

					this.outPort = null;
					this.inPort = null;
					getNodeGraph().update();
					return true;
				}
			}
		}
		return false;
	}

	public void removeConnection(GPort port) {
		for (GNodeConnection con : getAllEdges()) {
			if (con.getSourcePort() == port || con.getTargetPort() == port) {
				try {
					con.getTargetPort().getPortRectangle()
							.setInputColor(graph.getColorLookup().get(con.getTarget().getNodeType()));
					con.getTargetPort().getPortRectangle().redraw();
					con.getTargetPort().redraw();
					getNodeMaster().removeConnection(con.getTarget().getAbstractNode(),
							con.getTargetPort().getStringID());
				} catch (NoSuchNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				getRemovedEdges().add(con);
			}
		}
	}

	public void registerNodes(String packag) {
		this.nodeMaster.registerNodes(packag);
	}

	public void clear() {

		allNodes = new ArrayList<GNode>();
		addedNodes = new ArrayList<GNode>();
		removedNodes = new ArrayList<GNode>();

		allConnections = new ArrayList<GNodeConnection>();
		addedConnections = new ArrayList<GNodeConnection>();
		removedConnections = new ArrayList<GNodeConnection>();

		nodeMap = new HashMap<String, GNode>(); // <id,cell>

	}

	public NodeMaster getNodeMaster() {
		return nodeMaster;
	}

	public void clearAddedLists() {
		addedNodes.clear();
		addedConnections.clear();
	}

	public void removeNode(GNode node) {
		for (GNodeConnection c : getAllEdges()) {
			if (c.getSource() == node || c.getTarget() == node) {
				try {
					getNodeMaster().removeConnection(c.getTarget().getAbstractNode(), c.getTargetPort().getStringID());
				} catch (NoSuchNodeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				removedConnections.add(c);
			}
		}
		removedNodes.add(node);
		nodeMap.remove(node.getName());
		getNodeGraph().update();
	}

	public List<GNode> getAddedCells() {
		return addedNodes;
	}

	public List<GNode> getRemovedCells() {
		return removedNodes;
	}

	public List<GNode> getAllCells() {
		return allNodes;
	}

	public List<GNodeConnection> getAddedEdges() {
		return addedConnections;
	}

	public List<GNodeConnection> getRemovedEdges() {
		return removedConnections;
	}

	public List<GNodeConnection> getAllEdges() {
		return allConnections;
	}

	public void addNode(String id, INodeType type, boolean draw, GNodeGraph graph) {
		addNode(new GNode(id, type, draw, graph));

	}

	public void addNode(String id, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		GNode n = new GNode(id, type, draw, graph);
		n.relocate(x, y);
		addNode(n);

	}

	public void addNode(String id, Base type, boolean draw, GNodeGraph graph) {
		addNode(new GNode(id, type, draw, graph));
	}

	public void addNode(GNode cell) {
		addedNodes.add(cell);
		nodeMap.put(cell.getName(), cell);

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

	public boolean addConnection(GPort port1, GPort port2) {

		if (port1.getNode() == port2.getNode())
			return false;

		GNodeConnection con = new GNodeConnection(port1, port2);
		for (int i = 0; i < getAllEdges().size(); i++) {
			GNodeConnection c = getAllEdges().get(i);
			if (c.getSourcePort() == con.getSourcePort() && c.getTargetPort() == con.getTargetPort()) {
				this.outPort = null;
				this.inPort = null;
				return false;
			}
		}

		try {
			if (port1.isInput()) {
				getNodeMaster().setConnection(port1.getNode().getAbstractNode(), port1.getStringID(),
						port2.getNode().getAbstractNode(), port2.getStringID());
				port1.getPortRectangle().setInputColor(graph.getColorLookup().get(port2.getNode().getNodeType()));
				port1.getPortRectangle().redraw();
				port1.redraw();

			} else if (port2.isInput()) {
				getNodeMaster().setConnection(port2.getNode().getAbstractNode(), port2.getStringID(),
						port1.getNode().getAbstractNode(), port1.getStringID());
				port2.getPortRectangle().setInputColor(graph.getColorLookup().get(port1.getNode().getNodeType()));
				port2.getPortRectangle().redraw();
				port2.redraw();
			}

		} catch (NoSuchNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		port1.getNode().getConnections().add(con);
		port2.getNode().getConnections().add(con);
		addedConnections.add(con);
		return true;
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

		allNodes.addAll(addedNodes);
		allNodes.removeAll(removedNodes);

		allConnections.addAll(addedConnections);
		allConnections.removeAll(removedConnections);

		addedConnections.clear();
		removedConnections.clear();
		addedNodes.clear();
		removedNodes.clear();

	}

}
