package at.crimsonbit.nodesystem.gui.node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.gui.widget.toast.Toast;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastPosition;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastTime;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
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
import javafx.stage.Stage;

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
	private List<GNodeConnection> connections = new ArrayList<GNodeConnection>();

	private GPopUp popUpDialog;

	private Color topColor;
	private Color backColor;

	private boolean doDraw = false;
	private boolean active = false;
	private boolean portPressed = false;
	private INodeType type;
	private String nameAddition = "";
	private String name;
	private int inPortCount = 0;
	private int outPortcount = 0;
	private final int PORT_INPUT_START_X = 5;
	private final int PORT_INPUT_START_Y = 35;
	private int PORT_OUTPUT_START_X = 140;
	private int PORT_OUTPUT_START_Y = 35;
	private final int PORT_OFFSET = 40;
	private double height = 52;
	private final FileChooser fileChooser = new FileChooser();
	private int ppc = 0; // popup entry counter
	// private AbstractNode calcNode;
	private int nodeID;
	private Rectangle outline;
	private Rectangle base;
	private Rectangle top;
	private Text text;
	private DropShadow e;
	private final Tooltip tooltip = new Tooltip();

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
		tooltip.setText("Name: " + this.name + "\n" + "type: " + this.type.toString() + "\n");
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

			setView(base);
			if (active) {
				setView(outline);
			} else {
				removeView(outline);
			}
			setView(top);
			setView(text);

			for (GPort p : inputPorts) {
				removeView(p);
				setView(p);
				p.toFront();
			}
			for (GPort p : outputPorts) {
				removeView(p);
				setView(p);
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
		if (this.type == Base.OUTPUT) {
			pop.addItem(ppc++, "Get Output");
		} else if (this.type == Base.PATH) {
			pop.addItem(ppc++, "Set Path");
		}

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

		} else if (id == 4) {
			if (getNodeType() == Base.PATH) {
				setPath();
			} else
				setOutput();
			redraw();
			getNodeGraph().update();

		} else if (id == 0) {
			GNode node = new GNode(this);
			node.relocate(getBoundsInParent().getMinX(), getBoundsInParent().getMinY());
			nodeGraph.getGuiMaster().addNode(node);
			nodeGraph.update();

		}
	}

	public String getPath() {
		if (getNodeType().equals(Base.PATH)) {
			return (String) this.getAbstractNode().get("path");
		}

		return null;
	}

	public void setPath() {
		if (getNodeType().equals(Base.PATH)) {
			File f = fileChooser.showOpenDialog(getParent().getScene().getWindow());
			if (f != null)
				this.getAbstractNode().set("path", f.getPath());
		}
		System.out.println(getPath());
	}

	public Object getOutput() {
		if (getNodeType().equals(Base.OUTPUT)) {
			return this.getAbstractNode().get("output");
		}
		return null;
	}

	public void setOutput() {
		if (getNodeType().equals(Base.OUTPUT)) {
			setName("Output - " + String.valueOf(this.getAbstractNode().get("output")));
			computeNewPortLocations();
			redraw();
		}
	}

	public void computeNewPortLocations() {
		for (GPort p : getOutputPorts()) {
			p.redrawAndRelocate(this.name.length());
		}
	}

	public void setConstant() {

		doBlur();
		TextInputDialog dialog = new TextInputDialog(getName());
		dialog.setTitle("Constant");
		dialog.setHeaderText("Set a new constant for the node.");
		dialog.setContentText("Constant: ");

		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			// this.nameAddition = result.get();
			// setName(this.nameAddition);
			if (this.type == Constant.STRING) {
				this.getAbstractNode().set("constant", result.get());

			} else if (this.type == Constant.BOOLEAN) {
				try {
					boolean b = Boolean.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a boolean type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.DOUBLE) {
				try {
					double b = Double.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a double type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.FLOAT) {
				try {
					float b = Float.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a float type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.INTEGER) {
				try {
					int b = Integer.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a integer type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.BYTE) {
				try {
					byte b = Byte.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a byte type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}

			} else if (this.type == Constant.LONG) {
				try {
					long b = Long.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a long type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} else if (this.type == Constant.SHORT) {
				try {
					short b = Short.valueOf(result.get());
					this.getAbstractNode().set("constant", b);

				} catch (Exception e) {
					Toast.makeToast((Stage) getScene().getWindow(), "Invalid input!\nPlease type in a short type!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			}
			redraw();
			removeBlur();
		} else {
			removeBlur();

		}
	}

	private void setName(String string) {
		this.name = string;

	}

	public void addShape(Shape s) {
		shapes.add(s);
	}

	public List<Shape> getShapes() {
		return this.shapes;
	}

	public void addCellChild(GNode cell) {
		children.add(cell);
	}

	public List<GNode> getCellChildren() {
		return children;
	}

	public void addCellParent(GNode cell) {
		parents.add(cell);
	}

	public List<GNode> getCellParents() {
		return parents;
	}

	public void removeCellChild(GNode cell) {
		children.remove(cell);
	}

	public void setView(Node view) {

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
		// TODO Auto-generated method stub
		return this.portPressed;
	}

	@Override
	public void consumeCustomMessage(int id) {
		// TODO Auto-generated method stub

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

}
