package at.crimsonbit.nodesystem.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.animation.Animator;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.GNodeConnection;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.util.NodeConnection;

/**
 * 
 * @author Florian Wagner
 *
 */
public class GNodeMaster {

	private GNode graphParent;
	private NodeMaster nodeMaster;
	private Set<GNode> allNodes;

	private Set<GNode> addedNodes, removedNodes;

	private Set<GNodeConnection> allConnections;

	private Set<GNodeConnection> addedConnections, removedConnections;

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

		this.nodeMaster.setExtraInfoSavingFunction(this::saveData);

		// this.nodeMaster.registerNodes("at.crimsonbit.nodesystem.node.nodes");
		this.graphParent = new GNode("_ROOT_", false);

		// clear model, create lists

		clear();
	}

	/**
	 * Load Data for the provided AbstractNode
	 * 
	 * @param node
	 * @param data
	 */
	private void loadData(AbstractNode node, Object data) {
		for (GNode gn : allNodes) {
			if (gn.getNodeID() == getNodeMaster().getIdOfNode(node)) {
				gn.loadData(data);
			}
		}
	}

	/**
	 * Save Data of the provided AbstractNode
	 * 
	 * @param node
	 * @return
	 */
	private Object saveData(AbstractNode node) {
		for (GNode gn : allNodes) {
			if (gn.getNodeID() == getNodeMaster().getIdOfNode(node)) {
				return gn.storeData();
			}
		}
		return null;
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

			if (this.inPort.isConnected()) {
				this.inPort = null;
				this.outPort = null;
				return false;
			}

			if (addConnection(this.outPort, this.inPort)) {
				// System.out.println("INFO!");

				this.outPort = null;
				this.inPort = null;
				getNodeGraph().update();
				return true;

			}
		}
		return false;
	}

	@SuppressWarnings("unlikely-arg-type")
	public void removeConnection(GPort port) {
		GNodeConnection c = null;
		boolean succ = false;
		for (GNodeConnection con : getAllEdges()) {
			if (con.getSourcePort() == port || con.getTargetPort() == port) {
				c = con;
				try {
					con.getTargetPort().getPortRectangle()
							.setInputColor(graph.getGeneralColorLookup().get(GraphSettings.COLOR_PORT_INPUT));
					con.getTargetPort().getPortRectangle().redraw();
					con.getTargetPort().redraw();
					con.getTargetPort().setConnected(false);
					getNodeMaster().removeConnection(con.getTarget().getAbstractNode(),
							con.getTargetPort().getStringID());
					succ = true;
				} catch (NoSuchNodeException e) {

					e.printStackTrace();
				}

			}
		}
		if (c != null && succ) {
			allConnections.remove(c);
			removedConnections.add(c);
		}
	}

	public void registerNodes(String packag) {
		this.nodeMaster.registerNodes(packag);
	}

	public void clear() {

		allNodes = new HashSet<GNode>();

		addedNodes = new HashSet<GNode>();
		removedNodes = new HashSet<>();

		allConnections = new HashSet<GNodeConnection>();

		addedConnections = new HashSet<>();
		removedConnections = new HashSet<>();

		nodeMap = new HashMap<String, GNode>(); // <id,cell>

	}

	public NodeMaster getNodeMaster() {
		return nodeMaster;
	}

	public void removeNode(GNode node) {
		Iterator<GNodeConnection> iter = getAllEdges().iterator();
		while (iter.hasNext()) {
			GNodeConnection c = iter.next();
			if (c.getSource() == node || c.getTarget() == node) {
				try {

					getNodeMaster().removeConnection(c.getTarget().getAbstractNode(), c.getTargetPort().getStringID());
					c.getTargetPort().setConnected(false);
				} catch (NoSuchNodeException e) {
					e.printStackTrace();
				}
				iter.remove();
				removedConnections.add(c);
			}
		}
		getNodeMaster().deleteNode(node.getAbstractNode());
		allNodes.remove(node);
		nodeMap.remove(node.getName());
		removedNodes.add(node);
		getNodeGraph().update();
	}

	public Set<GNode> getAllCells() {
		return allNodes;
	}

	public Set<GNodeConnection> getAllEdges() {
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
		allNodes.add(cell);
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

		if (!port1.isInput() || port2.isInput()) {
			if (port2.isInput() && !port1.isInput())
				return addConnection(port2, port1);
			return false;
		}

		if (port1.getNode() == port2.getNode())
			return false;

		GNodeConnection con = new GNodeConnection(port2, port1);

		if (getAllEdges().contains(con)) {
			this.outPort = null;
			this.inPort = null;
			return false;
		}

		try {
			getNodeMaster().setConnection(port1.getNode().getAbstractNode(), port1.getStringID(),
					port2.getNode().getAbstractNode(), port2.getStringID());
			port1.getPortRectangle().setInputColor(graph.getColorLookup().get(port2.getNode().getNodeType()));
			port1.getPortRectangle().redraw();
			port1.redraw();
			port1.getNode().getConnections().add(con);
			port2.getNode().getConnections().add(con);
			port1.setConnected(true);
			allConnections.add(con);
			addedConnections.add(con);

		} catch (NoSuchNodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * Attach all cells which don't have a parent to graphParent
	 * 
	 * @param cellList
	 */
	public void attachOrphansToGraphParent(Collection<GNode> cellList) {

		for (GNode cell : cellList) {
			if (cell.getNodeParents().size() == 0) {
				graphParent.addNodeChildren(cell);
			}
		}

	}

	/**
	 * Remove the graphParent reference if it is set
	 * 
	 * @param cellList
	 */
	public void disconnectFromGraphParent(Collection<GNode> cellList) {

		for (GNode cell : cellList) {
			graphParent.removeCellChild(cell);
		}
	}

	protected void rebuild(NodeMaster master) throws NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		allNodes.clear();
		allConnections.clear();
		nodeMap.clear();

		Map<AbstractNode, GNode> cache = new HashMap<>();

		for (AbstractNode node : master.getAllNodes()) {
			Class<? extends GNode> clazz = getNodeGraph().getNodeMap().get(master.getTypeOfNode(node));
			Constructor<? extends GNode> constr = clazz.getConstructor(String.class, int.class, boolean.class,
					GNodeGraph.class);
			GNode gn = constr.newInstance(master.getTypeOfNode(node).toString(), master.getIdOfNode(node), true, graph);
			allNodes.add(gn);
			addedNodes.add(gn);
			cache.put(node, gn);
		}
		/*
		 * Add Connections
		 */
		for (AbstractNode node : master.getAllNodes()) {
			Map<Field, NodeConnection> connections = master.getConnections(node);
			GNode gn = cache.get(node);
			for (Map.Entry<Field, NodeConnection> con : connections.entrySet()) {
				GNode from = cache.get(con.getValue().getNodeInstance());
				List<GPort> inPorts = gn.getInputPorts();
				List<GPort> outPorts = from.getOutputPorts();
				GPort inPort = null, outPort = null;
				for (GPort p : inPorts) {
					if (p.getStringID().equals(con.getKey().getName())) {
						inPort = p;
						break;
					}
				}
				for (GPort p : outPorts) {
					if (p.getStringID().equals(con.getValue().getField().getName())) {
						outPort = p;
						break;
					}
				}
				this.addConnection(inPort, outPort);
			}
		}
		graph.update();
		master.getExtraInfo(this::loadData);
		master.setExtraInfoSavingFunction(this::saveData);
		graph.update();
	}

	public Set<GNode> getAddedCells() {
		return addedNodes;
	}

	public Set<GNodeConnection> getAddedEdges() {
		return addedConnections;
	}

	public Set<GNode> getRemovedCells() {
		return removedNodes;
	}

	public Set<GNodeConnection> getRemovedEdges() {
		return removedConnections;
	}

	public void removeAll() {
		removedConnections.addAll(allConnections);
		allConnections.clear();
		addedConnections.clear();

		removedNodes.addAll(allNodes);
		allNodes.clear();
		addedNodes.clear();
	}
}
