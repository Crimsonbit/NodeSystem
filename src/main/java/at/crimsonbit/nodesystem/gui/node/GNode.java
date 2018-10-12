package at.crimsonbit.nodesystem.gui.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.color.GColors;
import at.crimsonbit.nodesystem.gui.color.GStyle;
import at.crimsonbit.nodesystem.gui.color.GTheme;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.gui.settings.GGraphSettings;
import at.crimsonbit.nodesystem.gui.settings.GSettings;
import at.crimsonbit.nodesystem.language.LanguageSetup;
import at.crimsonbit.nodesystem.node.IGuiNodeType;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
import at.crimsonbit.nodesystem.util.RangeMapper;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;

/**
 * <h1>GNode</h1>
 * <p>
 * The base Node class. Every node of the NodeSystem is derived from this very
 * class. It has all the main features in it. To draw custom nodes all you have
 * to do is to override the draw function.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class GNode extends AnchorPane implements IGNode
{

	private GNodeGraph nodeGraph;
	private List<Shape> shapes = new ArrayList<Shape>();
	private List<GNode> children = new ArrayList<GNode>();
	private List<GNode> parents = new ArrayList<GNode>();
	protected List<GPort> inputPorts = new ArrayList<GPort>();
	protected List<GPort> outputPorts = new ArrayList<GPort>();
	protected List<GNodeConnection> connections = new ArrayList<GNodeConnection>();
	private LanguageSetup lang = LanguageSetup.getInstance();
	private Scale scaleTransform;
	private GPopUp popUpDialog;
	private Color topColor;
	private String typeName;
	protected boolean doDraw = false;
	protected boolean active = false;
	protected boolean hovered = false;
	private boolean portPressed = false;
	protected IGuiNodeType type;
	private String nameAddition = "";
	protected String name;
	protected int inPortCount = 0;
	protected int outPortcount = 0;
	private final int PORT_INPUT_START_X = 5;
	private final int PORT_INPUT_START_Y = 35;
	protected int PORT_OUTPUT_START_X = 150;
	private int PORT_OUTPUT_START_Y = 35;
	private final int PORT_OFFSET = 40;
	protected double height = 52;
	protected final FileChooser fileChooser = new FileChooser();
	private int ppc = 0; // popup entry counter
	// private AbstractNode calcNode;
	private int nodeID;
	private Rectangle outline;
	private Rectangle base;
	private Rectangle top;
	private Rectangle remove;
	private double scaleValue = 1.0;
	protected Text text;
	private DropShadow e;
	private final Tooltip tooltip = new Tooltip();
	private final int PORT_TOP_DRAW_OFFSET = 10;
	protected boolean toggledDraw = false;
	private double nodeX;
	private double nodeY;
	private boolean rename;

	private NodeMaster getNodeMaster()
	{
		return nodeGraph.getGuiMaster().getNodeMaster();
	}

	public GNode()
	{
		this("", false);

	}

	public GNode(String name, boolean draw)
	{
		this.nodeID = -1;
		this.name = name;
		this.doDraw = draw;
		defaultTopColor();
		defaultPopUpDialog();
		scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);

	}

	public GNode(String name, INodeType type, boolean draw, GNodeGraph graph)
	{
		this(name, graph.getGuiMaster().getNodeMaster().createNodeId(type), draw, graph);
	}

	public GNode(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y)
	{
		this(name, graph.getGuiMaster().getNodeMaster().createNodeId(type), draw, graph, x, y);
	}

	public GNode(String name, int id, boolean draw, GNodeGraph graph)
	{
		this.nodeID = id;
		this.name = name;
		this.nodeGraph = graph;
		this.type = (IGuiNodeType) graph.getGuiMaster().getNodeMaster().getTypeOfNode(id);
		this.typeName = this.type.toString();
		this.doDraw = draw;
		defaultTopColor();
		defaultPopUpDialog();
		addAllPorts();
		addToolTip();
		draw();
		scaleTransform = new Scale(scaleValue, scaleValue, 0, 0);

	}

	public GNode(String name, int id, boolean draw, GNodeGraph graph, double x, double y)
	{
		this(name, id, draw, graph);
		this.nodeX = x;
		this.nodeY = y;
		relocate(x, y);
	}

	public GNode(GNode gNode)
	{
		this(gNode.name, gNode.getNodeMaster().copyOfNode(gNode.nodeID), gNode.doDraw, gNode.nodeGraph);
	}

	public List<GNodeConnection> getConnections()
	{
		return this.connections;
	}

	public double getNodeX()
	{
		return nodeX;
	}

	public void setNodeX(double nodeX)
	{
		this.nodeX = nodeX;
	}

	public double getNodeY()
	{
		return nodeY;
	}

	public void setNodeY(double nodeY)
	{
		this.nodeY = nodeY;
	}

	public String getConnectionsString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("Connected to: ");
		for (GNodeConnection con : this.connections)
		{
			if (con.getSource().equals(this))
			{
				sb.append(con.getTarget().getName());
			}
		}
		return sb.toString();
	}

	public String getTypeName()
	{
		return this.typeName;
	}

	private void addToolTip()
	{
		tooltip.setText("Type: " + getNodeType().toString());
		Tooltip.install(this, tooltip);
	}

	public void setType(IGuiNodeType type)
	{
		this.type = type;
	}

	private void addAllPorts()
	{

		getInputPorts().clear();
		getOutputPorts().clear();
		for (String n : getNodeMaster().getAllInputNames(nodeID))
		{
			addInputPort(inPortCount, n, PORT_INPUT_START_X, PORT_INPUT_START_Y + (inPortCount * PORT_OFFSET));
			inPortCount++;
		}
		for (String n : getNodeMaster().getAllOutputNames(nodeID))
		{
			addOutputPort(inPortCount, n, PORT_OUTPUT_START_X, PORT_OUTPUT_START_Y + (outPortcount * PORT_OFFSET));
			outPortcount++;
		}
	}

	public boolean doDraw()
	{
		return doDraw;
	}

	public void setGNodeGraph(GNodeGraph graph)
	{
		this.nodeGraph = graph;
	}

	public Rectangle getTop()
	{
		return top;
	}

	public GNodeGraph getNodeGraph()
	{
		return this.nodeGraph;
	}

	public IGuiNodeType getNodeType()
	{
		return this.type;
	}

	public AbstractNode getAbstractNode()
	{
		return getNodeMaster().getNodeByID(nodeID);
	}

	public void setTopColor(Color c)
	{
		this.topColor = c;
		redraw();
	}

	public void setTopColor(double r, double g, double b)
	{
		this.topColor = new Color(r, g, b, 1);
		redraw();
	}

	private void defaultTopColor()
	{
		double tr = RangeMapper.mapValue(70, 0, 255, 0, 1);
		double tg = RangeMapper.mapValue(75, 0, 255, 0, 1);
		double tb = RangeMapper.mapValue(79, 0, 255, 0, 1);
		this.topColor = new Color(tr, tg, tb, 1);

	}

	public void setActive(boolean active)
	{
		this.active = active;
	}

	public boolean isActive()
	{
		return this.active;
	}

	public void addInputPort(GPort port)
	{
		inputPorts.add(port);
	}

	public void addInputPort(int id, String label, double x, double y)
	{
		inputPorts.add(new GPort(id, true, label, x, y, this));
	}

	public List<GPort> getInputPorts()
	{
		return inputPorts;
	}

	public void addOutputPort(GPort port)
	{
		outputPorts.add(port);
	}

	public void addOutputPort(int id, String label, double x, double y)
	{
		outputPorts.add(new GPort(id, false, label, x, y, this));
	}

	public GPort getOutputPortById(int id)
	{
		for (GPort port : getOutputPorts())
		{
			if (port.getID() == id)
			{
				return port;
			}
		}
		return null;
	}

	public GPort getInputPortById(int id)
	{
		for (GPort port : getInputPorts())
		{
			if (port.getID() == id)
			{
				return port;
			}
		}
		return null;
	}

	public List<GPort> getOutputPorts()
	{
		return outputPorts;
	}

	public void removeInputPort(GPort port)
	{
		inputPorts.remove(port);
	}

	public void removeOutputPort(GPort port)
	{
		outputPorts.remove(port);
	}

	public void drawNodeTop(double width)
	{
		double height = 50d / 3d;
		top = new Rectangle(width, height);

		if (GTheme.getInstance().getStyle().equals(GStyle.GRADIENT))
		{
			LinearGradient lg = GTheme.getInstance().getGradient(((IGuiNodeType) (type)).getColor());
			top.setStroke(lg);
			top.setFill(lg);
		} else if (GTheme.getInstance().getStyle().equals(GStyle.DEFAULT))
		{
			top.setStroke(((IGuiNodeType) (type)).getColor());
			top.setFill(((IGuiNodeType) (type)).getColor());
		} else if (GTheme.getInstance().getStyle().equals(GStyle.NO_COLOR))
		{

		} else
		{
			top = new Rectangle(width, 50d / 2d);
			top.setStroke(((IGuiNodeType) (type)).getColor());
			top.setFill(((IGuiNodeType) (type)).getColor());
		}
		top.setAccessibleText("node_top"); // top.setArcWidth(20.0); //
		top.setArcHeight(20.0);
		addView(top);

		Polygon poly = new Polygon();
		poly.setFill(GTheme.getInstance().getColor(GColors.COLOR_NODE_TRIANGLE_HOVERED));
		poly.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
			{
				poly.setFill(GTheme.getInstance().getColor(GColors.COLOR_NODE_TRIANGLE_DEFAULT));
			});
		poly.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
			{
				poly.setFill(GTheme.getInstance().getColor(GColors.COLOR_NODE_TRIANGLE_HOVERED));
			});
		poly.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
			{
				toggleDraw();
			});
		poly.addEventFilter(MouseEvent.MOUSE_RELEASED, event ->
			{
				redraw(true);
			});

		// if (!this.toggledDraw) {
		poly.getPoints().addAll(new Double[] { 10d, 5d, 2d, 5d, 6d, height / 1.2d });
		addView(poly);
		// }
	}

	public void drawNodeTopArc(double width, double arc)
	{
		double height = 50d / 3d;
		top = new Rectangle(width, height);
		top.setAccessibleText("node_top"); // top.setArcWidth(20.0); //
		top.setArcHeight(arc);
		top.setArcWidth(arc);

		if (GTheme.getInstance().getStyle().equals(GStyle.GRADIENT))
		{
			LinearGradient lg = GTheme.getInstance().getGradient(((IGuiNodeType) (type)).getColor());
			top.setStroke(lg);
			top.setFill(lg);
		} else if (GTheme.getInstance().getStyle().equals(GStyle.DEFAULT))
		{
			top.setStroke(((IGuiNodeType) (type)).getColor());
			top.setFill(((IGuiNodeType) (type)).getColor());
		} else if (GTheme.getInstance().getStyle().equals(GStyle.NO_COLOR))
		{

		} else
		{
			top = new Rectangle(width, 50d / 2d);
			top.setAccessibleText("node_top"); // top.setArcWidth(20.0); //
			top.setArcHeight(arc);
			top.setArcWidth(arc);
			top.setStroke(((IGuiNodeType) (type)).getColor());
			top.setFill(((IGuiNodeType) (type)).getColor());
		}

		addView(top);

		Polygon poly = new Polygon();
		poly.setFill(GTheme.getInstance().getColor(GColors.COLOR_NODE_TRIANGLE_HOVERED));
		poly.addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
			{
				poly.setFill(GTheme.getInstance().getColor(GColors.COLOR_NODE_TRIANGLE_DEFAULT));
			});
		poly.addEventHandler(MouseEvent.MOUSE_EXITED, event ->
			{
				poly.setFill(GTheme.getInstance().getColor(GColors.COLOR_NODE_TRIANGLE_HOVERED));
			});
		poly.addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
			{
				toggleDraw();
			});
		poly.addEventFilter(MouseEvent.MOUSE_RELEASED, event ->
			{
				redraw(true);
			});
		// if (this.toggledDraw) {
		double h1 = height / (1.2d);
		h1 /= 2;
		h1 += 1;
		poly.getPoints().addAll(new Double[] { 2d, 3d, 2d, (height / 1.2d) - 2, 10d, h1 });
		addView(poly);
		// }

	}

	public void drawNodeBase(double width, double height)
	{
		base = new Rectangle(width, height);
		Color c = GTheme.getInstance().getColor(GColors.COLOR_NODE);
		base.setStroke(c);
		base.setFill(c);
		base.setAccessibleText("node_base");
		base.setArcWidth(20.0);
		base.setArcHeight(20.0);
		addView(base);
	}

	public void drawNodeOutline(double width, double height, boolean active, boolean toggled, boolean hovered)
	{

		outline = new Rectangle(width, height);
		// outline.setTranslateY(5);
		outline.setFill(Color.TRANSPARENT);
		outline.setStrokeWidth(0d);
		if (hovered)
		{
			outline.setStroke(Color.DARKORANGE);
			outline.setStrokeWidth(2d);
			
		} else if (toggled)
		{
			outline = new Rectangle(width, height);
			if (GTheme.getInstance().getStyle().equals(GStyle.DARK))
				outline = new Rectangle(width, 50d / 2d);

			outline.setFill(Color.TRANSPARENT);
			outline.setStroke(GTheme.getInstance().getColor(GColors.COLOR_ACTIVE_TOGGLED));
			outline.setStrokeWidth(1);

		}

		outline.setArcWidth(21.0);
		outline.setArcHeight(21.0);

		if (active || hovered)
		{
			addView(outline);
		} else
		{
			removeView(outline);
		}

	}

	public double drawNodeText(String name)
	{
		if (!this.rename)
			text = new Text(type.toString());
		else
			text = new Text(name);

		text.setFill(GTheme.getInstance().getColor(GColors.COLOR_TEXT_COLOR));
		text.setTranslateY(12.5);
		text.setTranslateX(15);
		if (GTheme.getInstance().getStyle().equals(GStyle.DARK))
		{
			text.setTranslateY(33 - text.getBoundsInLocal().getHeight());
			text.setTranslateX(150 - text.getBoundsInLocal().getWidth());
		}
		return text.getBoundsInLocal().getWidth();
	}

	public void drawNodeShadow()
	{
		e = new DropShadow();
		e.setBlurType(BlurType.GAUSSIAN);
		e.setColor(GTheme.getInstance().getColor(GColors.COLOR_SHADOW_COLOR));
		GGraphSettings inst = GGraphSettings.getInstance();
		e.setWidth((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
		e.setHeight((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
		e.setOffsetX((double) inst.getSetting(GSettings.SETTING_SHADOW_WIDTH));
		e.setOffsetY((double) inst.getSetting(GSettings.SETTING_SHADOW_HEIGHT));
		e.setRadius((double) inst.getSetting(GSettings.SETTING_SHADOW_RADIUS));
		base.setEffect(e);
	}

	public void toggleDraw()
	{
		toggledDraw = !toggledDraw;

	}

	@Override
	public void draw()
	{

		if (this.doDraw)
		{
			if (!toggledDraw)
			{
				double h = height * inPortCount;
				if (inPortCount < outPortcount)
				{
					h = height * outPortcount;
				}

				double width = 150;
				double tWidth = drawNodeText(name);

				if (width < tWidth)
					width = tWidth;
				PORT_OUTPUT_START_X = (int) width;

				drawNodeBase(width, h);
				drawNodeOutline(width, h, active, toggledDraw, hovered);

				drawNodeTop(width);
				addView(text);
				for (GPort p : inputPorts)
				{
					removeView(p);
					addView(p);
					p.toFront();
				}
				for (GPort p : outputPorts)
				{
					removeView(p);
					addView(p);
					p.toFront();
				}

				computeUnToggledPortLocations();

			} else
			{
				double width = 150;
				double tWidth = drawNodeText(name);
				if (width < tWidth)
					width = tWidth;
				drawNodeTopArc(width, 15.0);
				drawNodeOutline(width, 50 / 3, active, toggledDraw, hovered);
				addView(text);
				drawToggledConnections((12.5d / 2d) - 1d);
			}
		} else

		{
			getChildren().clear();
		}
	}

	public void redraw()
	{
		redraw(false);
		redraw(true);
	}

	public void redraw(boolean draw)
	{
		this.doDraw = draw;
		draw();
		computeNewPortLocations();

	}

	public void setPopUpDialog(GPopUp dialog)
	{
		this.popUpDialog = dialog;
		addPopUpHandler();
	}

	public GPopUp getPopUpDialog()
	{
		return this.popUpDialog;
	}

	public void addPopUpItem(int id, String name)
	{
		if (id > ppc)
			this.popUpDialog.addItem(id, name);
		addPopUpItemHandler();
	}

	public int getInternalIDCounter()
	{
		return ppc;
	}

	private void defaultPopUpDialog()
	{
		GPopUp pop = new GPopUp();
		pop.addItem(-2, getName(), true);
		pop.addSeparator(-1);
		pop.addItem(ppc++, lang.getString("core", "nodePopCopy"));
		pop.addItem(ppc++, lang.getString("core", "nodePopRemoveActive"));
		pop.addItem(ppc++, lang.getString("core", "nodePopReName"));
		pop.addItem(ppc++, lang.getString("core", "nodePopRemove"));

		// if (this.type == Base.OUTPUT) {
		// pop.addItem(ppc++, "Get Output");
		// } else if (this.type == Base.PATH) {
		// pop.addItem(ppc++, "Set Path");
		// }

		setPopUpDialog(pop);
	}

	private void addPopUpItemHandler()
	{
		for (MenuItem item : this.popUpDialog.getItems())
		{
			int id = Integer.valueOf(item.getId());
			item.setOnAction(event ->
				{
					if (id <= ppc)
						consumeMessage(id);
					else
						consumeCustomMessage(id);
					event.consume();
				});
		}
	}

	private void addPopUpHandler()
	{
		addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event ->
			{
				this.popUpDialog.show(this, event.getScreenX(), event.getScreenY());
				event.consume();
			});

		addEventHandler(MouseEvent.MOUSE_PRESSED, event ->
			{
				this.popUpDialog.hide();
			});
		addPopUpItemHandler();
	}

	public void consumeMessage(int id)
	{
		if (id == 3)
		{
			nodeGraph.getGuiMaster().removeNode(this);
			nodeGraph.update();
		} else if (id == 1)
		{
			nodeGraph.update();
			setActive(false);
			redraw();
		} else if (id == 2)
		{
			nodeGraph.doBlur();
			TextInputDialog dialog = new TextInputDialog(getName());
			dialog.setTitle(lang.getString("core", "nodePopRemove"));
			dialog.setHeaderText(lang.getString("core", "nodeReMenuText"));
			dialog.setContentText(lang.getString("core", "nodeReMenuContent"));
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent())
			{
				setName(result.get() + nameAddition);
				nodeGraph.removeBlur();
			} else
			{
				nodeGraph.removeBlur();
			}
			redraw();

			// } else if (id == 4) {
			// if (getNodeType() == Base.PATH) {
			// setPath();
			// } else
			// setOutput();
			// redraw();
			// getNodeGraph().update();

		} else if (id == 0)
		{
			getNodeGraph().getClipBoard().copyPaste(this);
			/*
			 * GNode node = new GNode(this); node.relocate(getBoundsInParent().getMinX(),
			 * getBoundsInParent().getMinY()); nodeGraph.getGuiMaster().addNode(node);
			 * nodeGraph.update();
			 */
		}
	}

	protected void drawToggledConnections(double y)
	{
		if (getOutputPorts().size() >= 1)
			for (GPort p : getOutputPorts())
			{
				p.relocatePortY(y);
				p.redraw();
				List<GNodeConnection> conn = getConnections();
				conn.stream().filter(con -> getOutputPorts().contains(con.getSourcePort())).forEach(con ->
					{
						con.update(con.getSourcePort(), con.getTargetPort());
					});
			}
		for (GPort p : getInputPorts())
		{
			p.relocatePortY(y);
			p.redraw();
			List<GNodeConnection> conn = getConnections();
			conn.stream().filter(con -> getInputPorts().contains(con.getTargetPort())).forEach(con ->
				{
					con.update(con.getSourcePort(), con.getTargetPort());
				});
		}
		// conn.stream().filter(con ->
		// getInputPorts().contains(con.getTargetPort())).forEach(con -> {
		// con.draw(1000);
		// });
	}

	protected void computeUnToggledPortLocations()
	{
		int inPortCount = 0;
		int outPortCount = 0;
		for (GPort p : getOutputPorts())
		{
			p.relocatePortX(this.top.getWidth() - PORT_TOP_DRAW_OFFSET);
			p.relocatePortY(PORT_INPUT_START_Y + (outPortCount * PORT_OFFSET));
			p.redraw();
			List<GNodeConnection> conn = getConnections();
			conn.stream().filter(con -> getOutputPorts().contains(con.getSourcePort())).forEach(con ->
				{
					con.update(con.getSourcePort(), con.getTargetPort());
				});
			outPortCount++;
		}
		for (GPort p : getInputPorts())
		{
			// p.relocatePortX(this.top.getWidth() - PORT_TOP_DRAW_OFFSET);
			p.relocatePortY(PORT_INPUT_START_Y + (inPortCount * PORT_OFFSET));
			p.redraw();
			List<GNodeConnection> conn = getConnections();
			conn.stream().filter(con -> getInputPorts().contains(con.getTargetPort())).forEach(con ->
				{
					con.update(con.getSourcePort(), con.getTargetPort());
				});
			inPortCount++;
		}
	}

	protected void computeNewPortLocations()
	{
		for (GPort p : getOutputPorts())
		{
			p.relocatePortX(this.top.getWidth() - PORT_TOP_DRAW_OFFSET);
			p.redraw();
			List<GNodeConnection> conn = getConnections();
			conn.stream().filter(con -> getOutputPorts().contains(con.getSourcePort())).forEach(con ->
				{
					con.update(con.getSourcePort(), con.getTargetPort());
				});
		}
	}

	public void setName(String string)
	{
		this.rename = true;
		this.name = string;
	}

	public void addShape(Shape s)
	{
		shapes.add(s);
	}

	public List<Shape> getShapes()
	{
		return this.shapes;
	}

	public void addNodeChildren(GNode cell)
	{
		children.add(cell);
	}

	public List<GNode> getNodeChildren()
	{
		return children;
	}

	public void addNodeParent(GNode cell)
	{
		parents.add(cell);
	}

	public List<GNode> getNodeParents()
	{
		return parents;
	}

	public void removeCellChild(GNode cell)
	{
		children.remove(cell);
	}

	public void addView(Node view)
	{
		getChildren().add(view);

	}

	public void removeView(Node view)
	{
		getChildren().remove(view);
	}

	public String getName()
	{
		return name;
	}

	public void setPortPressed(boolean pressed)
	{
		this.portPressed = pressed;
	}

	public boolean isPortPressed()
	{
		return this.portPressed;
	}

	@Override
	public void consumeCustomMessage(int id)
	{

	}

	public boolean isHovered()
	{
		return hovered;
	}

	public void setHovered(boolean hovered)
	{
		this.hovered = hovered;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeGraph == null) ? 0 : nodeGraph.hashCode());
		result = prime * result + nodeID;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GNode other = (GNode) obj;
		if (nodeGraph == null)
		{
			if (other.nodeGraph != null)
				return false;
		} else if (!nodeGraph.equals(other.nodeGraph))
			return false;
		if (nodeID != other.nodeID)
			return false;
		if (type == null)
		{
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	public int getNodeID()
	{
		return nodeID;
	}

	@Override
	public String toString()
	{
		return name + ", type: " + type + ", ppc: " + ppc + ", connections: " + getConnectionsString();
	}

	/**
	 * Load Data, that was previously stored in {@link GNode#storeData()} again.
	 * Override to save custom data in GNode
	 * 
	 * @param data
	 */
	@SuppressWarnings("unchecked")
	public void loadData(Object data)
	{
		Data d = (Data) data;
		relocate(d.x, d.y);
		this.setName(d.name);
		redraw();
	}

	/**
	 * Store Data, that will be loaded when the Node System is loaded from a
	 * savefile. This function is called just before the data is saved
	 * 
	 * @see GNode#loadData(Object)
	 * @return
	 */
	public Object storeData()
	{
		return new Data(this.getLayoutX(), this.getLayoutY(), this.getName());
	}

	private static class Data implements Serializable
	{

		private static final long SerialVersionUID = 0;

		public final double x, y;
		public final String name;

		public Data(double x, double y, String name)
		{
			super();
			this.x = x;
			this.y = y;
			this.name = name;
		}

	}

	public void onKeyPressed(KeyEvent event)
	{

	}

}
