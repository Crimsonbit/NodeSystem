package at.crimsonbit.nodesystem.gui.node;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
import at.crimsonbit.nodesystem.nodebackend.util.Tuple;
import at.crimsonbit.nodesystem.util.RangeMapper;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * 
 * @author Florian Wagner
 *
 */
public class GNode extends Pane implements IGNode {

	private GNodeGraph nodeGraph;
	private List<Shape> shapes = new ArrayList<Shape>();
	private List<GNode> children = new ArrayList<GNode>();
	private List<GNode> parents = new ArrayList<GNode>();
	private List<GPort> inputPorts = new ArrayList<GPort>();
	private List<GPort> outputPorts = new ArrayList<GPort>();
	protected List<GNodeConnection> connections = new ArrayList<GNodeConnection>();

	private GPopUp popUpDialog;

	private Color topColor;
	private Color backColor;

	private boolean doDraw = false;
	private boolean active = false;
	private boolean portPressed = false;
	protected INodeType type;
	private String nameAddition = "";
	protected String name;
	protected int inPortCount = 0;
	protected int outPortcount = 0;
	private final int PORT_INPUT_START_X = 5;
	private final int PORT_INPUT_START_Y = 35;
	private int PORT_OUTPUT_START_X = 140;
	private int PORT_OUTPUT_START_Y = 35;
	private final int PORT_OFFSET = 40;
	private double height = 52;
	protected final FileChooser fileChooser = new FileChooser();
	private int ppc = 0; // popup entry counter
	// private AbstractNode calcNode;
	private int nodeID;
	private Rectangle outline;
	private Rectangle base;
	private Rectangle top;
	private Text text;
	private DropShadow e;
	private final Tooltip tooltip = new Tooltip();
	private final int PORT_TOP_DRAW_OFFSET = 10;

	private NodeMaster getNodeMaster() {
		return nodeGraph.getGuiMaster().getNodeMaster();
	}

	public GNode() {
		this("", false);
	}

	public GNode(String name, boolean draw) {
		this.nodeID = -1;
		this.name = name;
		this.doDraw = draw;
		defaultTopColor();
		defaultBackColor();
		defaultPopUpDialog();
	}

	public GNode(String name, INodeType type, boolean draw, GNodeGraph graph) {
		this(name, graph.getGuiMaster().getNodeMaster().createNodeId(type), draw, graph);
	}

	public GNode(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		this(name, graph.getGuiMaster().getNodeMaster().createNodeId(type), draw, graph, x, y);
	}

	public GNode(String name, int id, boolean draw, GNodeGraph graph) {
		this.nodeID = id;
		this.name = name;
		this.nodeGraph = graph;
		this.type = graph.getGuiMaster().getNodeMaster().getTypeOfNode(id);
		this.doDraw = draw;
		defaultTopColor();
		defaultBackColor();
		defaultPopUpDialog();
		addAllPorts();
		addToolTip();
		draw();
	}

	public GNode(String name, int id, boolean draw, GNodeGraph graph, double x, double y) {
		this(name, id, draw, graph);
		relocate(x, y);
	}

	public GNode(GNode gNode) {
		this(gNode.name, gNode.getNodeMaster().copyOfNode(gNode.nodeID), gNode.doDraw, gNode.nodeGraph);
	}

	public List<GNodeConnection> getConnections() {
		return this.connections;
	}

	private void addToolTip() {
		tooltip.setText(
				"Name: " + name + "\nType: " + getNodeType().toString() + "\nConnections: " + connections.size());
		Tooltip.install(this, tooltip);
	}

	private void addAllPorts() {

		getInputPorts().clear();
		getOutputPorts().clear();
		for (String n : getNodeMaster().getAllInputNames(nodeID)) {
			addInputPort(inPortCount, n, PORT_INPUT_START_X, PORT_INPUT_START_Y + (inPortCount * PORT_OFFSET));
			inPortCount++;
		}
		for (String n : getNodeMaster().getAllOutputNames(nodeID)) {
			addOutputPort(inPortCount, n, PORT_OUTPUT_START_X, PORT_OUTPUT_START_Y + (outPortcount * PORT_OFFSET));
			outPortcount++;
		}
	}

	public boolean doDraw() {
		return doDraw;
	}

	public void setGNodeGraph(GNodeGraph graph) {
		this.nodeGraph = graph;
	}

	public Rectangle getTop() {
		return top;
	}

	public GNodeGraph getNodeGraph() {
		return this.nodeGraph;
	}

	public INodeType getNodeType() {
		return this.type;
	}

	public AbstractNode getAbstractNode() {
		return getNodeMaster().getNodeByID(nodeID);
	}

	public void setBackColor(Color c) {
		this.backColor = c;
		redraw();
	}

	public void setBackColor(double r, double g, double b) {
		this.backColor = new Color(r, g, b, 1);
		redraw();
	}

	private void defaultBackColor() {
		double br = RangeMapper.mapValue(38, 0, 255, 0, 1);
		double bg = RangeMapper.mapValue(43, 0, 255, 0, 1);
		double bb = RangeMapper.mapValue(47, 0, 255, 0, 1);
		this.backColor = new Color(br, bg, bb, 1);

	}

	public void setTopColor(Color c) {
		this.topColor = c;
		redraw();
	}

	public void setTopColor(double r, double g, double b) {
		this.topColor = new Color(r, g, b, 1);
		redraw();
	}

	private void defaultTopColor() {
		double tr = RangeMapper.mapValue(70, 0, 255, 0, 1);
		double tg = RangeMapper.mapValue(75, 0, 255, 0, 1);
		double tb = RangeMapper.mapValue(79, 0, 255, 0, 1);
		this.topColor = new Color(tr, tg, tb, 1);

	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return this.active;
	}

	public void addInputPort(GPort port) {
		inputPorts.add(port);
	}

	public void addInputPort(int id, String label, double x, double y) {
		inputPorts.add(new GPort(id, true, label, x, y, this));
	}

	public List<GPort> getInputPorts() {
		return inputPorts;
	}

	public void addOutputPort(GPort port) {
		outputPorts.add(port);
	}

	public void addOutputPort(int id, String label, double x, double y) {
		outputPorts.add(new GPort(id, false, label, x, y, this));
	}

	public GPort getOutputPortById(int id) {
		for (GPort port : getOutputPorts()) {
			if (port.getID() == id) {
				return port;
			}
		}
		return null;
	}

	public GPort getInputPortById(int id) {
		for (GPort port : getInputPorts()) {
			if (port.getID() == id) {
				return port;
			}
		}
		return null;
	}

	public List<GPort> getOutputPorts() {
		return outputPorts;
	}

	public void removeInputPort(GPort port) {
		inputPorts.remove(port);
	}

	public void removeOutputPort(GPort port) {
		outputPorts.remove(port);
	}

	@Override
	public void draw() {

		if (this.doDraw) {

			double h = height * inPortCount;
			if (inPortCount < outPortcount) {
				h = height * outPortcount;
			}

			double width = 150;
			text = new Text(name);
			text.setFill(nodeGraph.getGeneralColorLookup().get(GraphSettings.COLOR_TEXT_COLOR));
			text.setTranslateY(12.5);
			double tWidth = text.getBoundsInLocal().getWidth();
			if (width < tWidth)
				width = tWidth;

			PORT_OUTPUT_START_X = (int) width;
			// addAllNodes();

			outline = new Rectangle(width, h - 5);
			outline.setTranslateY(5);
			outline.setFill(Color.TRANSPARENT);
			outline.setStroke(nodeGraph.getGeneralColorLookup().get(GraphSettings.COLOR_NODE_ACTIVE));
			outline.setArcWidth(21.0);
			outline.setArcHeight(21.0);
			outline.setStrokeWidth(1);

			base = new Rectangle(width, h);
			base.setStroke(backColor);
			base.setFill(backColor);
			base.setAccessibleText("node_base");
			base.setArcWidth(20.0);
			base.setArcHeight(20.0);

			top = new Rectangle(width, 50 / 3);

			top.setStroke(topColor);
			top.setFill(topColor);
			top.setAccessibleText("node_top"); // top.setArcWidth(20.0); //
			top.setArcHeight(20.0);

			top.setStroke(nodeGraph.getColorLookup().get(type));
			top.setFill(nodeGraph.getColorLookup().get(type));

			// text.setTranslateX(35);

			e = new DropShadow();
			e.setBlurType(BlurType.GAUSSIAN);
			double col = (double) getNodeGraph().getSettings().get(GraphSettings.COLOR_SHADOW_COLOR);
			e.setColor(new Color(col, col, col, 1));
			e.setWidth((double) getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
			e.setHeight((double) getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
			e.setOffsetX((double) getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
			e.setOffsetY((double) getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
			e.setRadius((double) getNodeGraph().getSettings().get(GraphSettings.SETTING_SHADOW_RADIUS));
			base.setEffect(e);

			addView(base);
			if (active) {
				addView(outline);
			} else {
				removeView(outline);
			}

			addView(top);
			addView(text);

			for (GPort p : inputPorts) {
				removeView(p);
				addView(p);
				p.toFront();
			}
			for (GPort p : outputPorts) {
				removeView(p);
				addView(p);
				p.toFront();
			}

		} else {
			getChildren().clear();
		}
	}

	public void redraw() {
		redraw(false);
		redraw(true);
	}

	public void redraw(boolean draw) {
		this.doDraw = draw;
		draw();
		computeNewPortLocations();

	}

	public void setPopUpDialog(GPopUp dialog) {
		this.popUpDialog = dialog;
		addPopUpHandler();
	}

	public GPopUp getPopUpDialog() {
		return this.popUpDialog;
	}

	public void addPopUpItem(int id, String name) {
		if (id > ppc)
			this.popUpDialog.addItem(id, name);
		addPopUpItemHandler();
	}

	public int getInternalIDCounter() {
		return ppc;
	}

	private void defaultPopUpDialog() {
		GPopUp pop = new GPopUp();
		pop.addItem(-2, getName(), true);
		pop.addSeparator(-1);
		pop.addItem(ppc++, "Copy Node");
		pop.addItem(ppc++, "remove Active");
		pop.addItem(ppc++, "Rename");
		pop.addItem(ppc++, "Remove");

		// if (this.type == Base.OUTPUT) {
		// pop.addItem(ppc++, "Get Output");
		// } else if (this.type == Base.PATH) {
		// pop.addItem(ppc++, "Set Path");
		// }

		setPopUpDialog(pop);
	}

	private void addPopUpItemHandler() {
		for (MenuItem item : this.popUpDialog.getItems()) {
			int id = Integer.valueOf(item.getId());
			item.setOnAction(event -> {
				if (id <= ppc)
					consumeMessage(id);
				else
					consumeCustomMessage(id);
				event.consume();
			});
		}
	}

	private void addPopUpHandler() {
		addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
			this.popUpDialog.show(this, event.getScreenX(), event.getScreenY());
			event.consume();
		});

		addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			this.popUpDialog.hide();
		});
		addPopUpItemHandler();
	}

	public void doBlur() {
		BoxBlur blur = new BoxBlur(3, 3, 3);
		getParent().setEffect(blur);
		nodeGraph.setEffect(blur);
	}

	public void removeBlur() {
		getParent().setEffect(null);
		nodeGraph.setEffect(null);
	}

	public void consumeMessage(int id) {
		if (id == 3) {
			nodeGraph.getGuiMaster().removeNode(this);
			nodeGraph.update();
		} else if (id == 1) {
			nodeGraph.update();
			setActive(false);
			redraw();
		} else if (id == 2) {
			doBlur();
			TextInputDialog dialog = new TextInputDialog(getName());
			dialog.setTitle("Name");
			dialog.setHeaderText("Set a new name for the node.");
			dialog.setContentText("Name: ");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				setName(result.get() + nameAddition);
				removeBlur();
			} else {
				removeBlur();
			}
			redraw();

			// } else if (id == 4) {
			// if (getNodeType() == Base.PATH) {
			// setPath();
			// } else
			// setOutput();
			// redraw();
			// getNodeGraph().update();

		} else if (id == 0) {
			GNode node = new GNode(this);
			node.relocate(getBoundsInParent().getMinX(), getBoundsInParent().getMinY());
			nodeGraph.getGuiMaster().addNode(node);
			nodeGraph.update();

		}
	}

	protected void computeNewPortLocations() {
		for (GPort p : getOutputPorts()) {
			p.redrawAndRelocate(this.top.getWidth() - PORT_TOP_DRAW_OFFSET);
			p.redraw();
			List<GNodeConnection> conn = getConnections();
			conn.stream().filter(con -> getOutputPorts().contains(con.getSourcePort())).forEach(con -> {
				// System.out.println(con);
				// con.relocate(this.top.getWidth(), con.sourcePort.getY());
				con.draw();
			});
		}
	}

	protected void setName(String string) {
		this.name = string;
	}

	public void addShape(Shape s) {
		shapes.add(s);
	}

	public List<Shape> getShapes() {
		return this.shapes;
	}

	public void addNodeChildren(GNode cell) {
		children.add(cell);
	}

	public List<GNode> getNodeChildren() {
		return children;
	}

	public void addNodeParent(GNode cell) {
		parents.add(cell);
	}

	public List<GNode> getNodeParents() {
		return parents;
	}

	public void removeCellChild(GNode cell) {
		children.remove(cell);
	}

	public void addView(Node view) {
		getChildren().add(view);
	}

	public void removeView(Node view) {
		getChildren().remove(view);
	}

	public String getName() {
		return name;
	}

	public void setPortPressed(boolean pressed) {
		this.portPressed = pressed;
	}

	public boolean isPortPressed() {
		return this.portPressed;
	}

	@Override
	public void consumeCustomMessage(int id) {

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeGraph == null) ? 0 : nodeGraph.hashCode());
		result = prime * result + nodeID;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GNode other = (GNode) obj;
		if (nodeGraph == null) {
			if (other.nodeGraph != null)
				return false;
		} else if (!nodeGraph.equals(other.nodeGraph))
			return false;
		if (nodeID != other.nodeID)
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public int getNodeID() {
		return nodeID;
	}

	@Override
	public String toString() {
		return name + ", connections=" + connections + ", type=" + type + ", inPortCount=" + inPortCount
				+ ", outPortcount=" + outPortcount + ", ppc=" + ppc;
	}

	/**
	 * Load Data, that was previously stored in {@link GNode#storeData()} again.
	 * Override to save custom data in GNode
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void loadData(Object data) {
		Tuple<Double, Double> tu = (Tuple<Double, Double>) data;
		relocate(tu.a, tu.b);
	}

	/**
	 * Store Data, that will be loaded when the Node System is loaded from a
	 * savefile. This function is called just before the data is saved
	 * 
	 * @see GNode#loadData(Object)
	 * @return
	 */
	public Object storeData() {
		return new Tuple<Double, Double>(this.getLayoutX(), this.getLayoutY());
	}

}
