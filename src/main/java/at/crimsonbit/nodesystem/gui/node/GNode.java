package at.crimsonbit.nodesystem.gui.node;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.crimsonbit.nodesystem.gui.GNodeGraph;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.node.port.GPort;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.nodebackend.api.AbstractNode;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
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
 * @author NeonArtworks
 *
 */
public class GNode extends Pane implements IGNode {

	private GNodeGraph nodeGraph;
	private List<Shape> shapes = new ArrayList<Shape>();
	private List<GNode> children = new ArrayList<GNode>();
	private List<GNode> parents = new ArrayList<GNode>();
	private List<GPort> inputPorts = new ArrayList<GPort>();
	private List<GPort> outputPorts = new ArrayList<GPort>();

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

	private AbstractNode calcNode;

	private Rectangle outline;
	private Rectangle base;
	private Rectangle top;
	private Text text;
	private DropShadow e;
	private final Tooltip tooltip = new Tooltip();

	public GNode(String name, boolean draw) {
		this.doDraw = draw;
		this.name = name;
		defaultTopColor();
		defaultBackColor();
		defaultPopUpDialog();
		draw();
	}

	public GNode(String name, INodeType type, boolean draw, GNodeGraph graph) {
		this.nodeGraph = graph;
		this.doDraw = draw;
		this.name = name;
		this.type = type;
		this.calcNode = this.nodeGraph.getGuiMaster().getNodeMaster().createNode(type);
		defaultTopColor();
		defaultBackColor();
		defaultPopUpDialog();
		addAllNodes();
		addToolTip();
		draw();
	}

	public GNode(String name, INodeType type, boolean draw, GNodeGraph graph, double x, double y) {
		this.nodeGraph = graph;
		this.doDraw = draw;
		this.name = name;
		this.type = type;
		this.calcNode = this.nodeGraph.getGuiMaster().getNodeMaster().createNode(type);
		defaultTopColor();
		defaultBackColor();
		defaultPopUpDialog();
		addAllNodes();
		relocate(x, y);
		addToolTip();
		draw();
	}

	public GNode(GNode gNode) {
		this.nodeGraph = gNode.getNodeGraph();
		this.doDraw = gNode.doDraw();
		this.name = gNode.getName();
		this.type = gNode.getNodeType();
		this.calcNode = this.nodeGraph.getGuiMaster().getNodeMaster().createNode(type);
		defaultTopColor();
		defaultBackColor();
		defaultPopUpDialog();

		addAllNodes();
		addToolTip();
		draw();
	}

	private void addToolTip() {
		tooltip.setText("Name: " + this.name + "\n" + "type: " + this.type.toString() + "\n");
		Tooltip.install(this, tooltip);
	}

	private void addAllNodes() {

		getInputPorts().clear();
		getOutputPorts().clear();
		for (String n : calcNode.getNodeMaster().getAllInputNames(calcNode)) {
			addInputPort(inPortCount, n, PORT_INPUT_START_X, PORT_INPUT_START_Y + (inPortCount * PORT_OFFSET));
			inPortCount++;
		}
		for (String n : calcNode.getNodeMaster().getAllOutputNames(calcNode)) {
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

	public AbstractNode getNode() {
		return calcNode;
	}

	public void setBackColor(Color c) {
		this.backColor = c;
		redraw();
	}

	public void setBackColor(double r, double g, double b) {
		this.backColor = new Color(r, g, b, 1);
		redraw();
	}

	private void defaultPopUpDialog() {
		GPopUp pop = new GPopUp();
		pop.addItem(-2, getName(), true);
		pop.addSeparator(-1);
		pop.addItem(6, "Copy Node");
		pop.addItem(2, "remove Active");
		pop.addItem(3, "Rename");
		pop.addItem(0, "Remove");
		if (this.type == Base.OUTPUT) {
			pop.addItem(4, "Get Output");
		}

		else if (this.type == Base.CONSTANT) {
			pop.addItem(5, "Set Constant");
		} else if (this.type == Base.PATH) {
			pop.addItem(7, "Set Path");
		}
		// pop.addItem(1, "set Active");

		setPopUpDialog(pop);
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
			text.setFill(nodeGraph.getGeneralColorLookup().get("text"));
			text.setTranslateY(12.5);
			double tWidth = text.getBoundsInLocal().getWidth();
			if (width < tWidth)
				width = tWidth;

			PORT_OUTPUT_START_X = (int) width;
			// addAllNodes();

			outline = new Rectangle(width, h - 5);
			outline.setTranslateY(5);
			outline.setFill(Color.TRANSPARENT);
			outline.setStroke(nodeGraph.getGeneralColorLookup().get("active"));
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
			e.setColor(new Color(0.1, 0.1, 0.1, 1));
			e.setWidth(5);
			e.setHeight(5);
			e.setOffsetX(5);
			e.setOffsetY(5);
			e.setRadius(10);
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
		addPopUpHandler(this.popUpDialog);
	}

	public GPopUp getPopUpDialog() {
		return this.popUpDialog;
	}

	private void addPopUpHandler(GPopUp dialog) {
		this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
			dialog.show(this, event.getScreenX(), event.getScreenY());
			event.consume();
		});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			dialog.hide();
		});

		for (MenuItem item : this.popUpDialog.getItems()) {
			int id = Integer.valueOf(item.getId());
			item.setOnAction(event -> {
				consumeMessage(id);
				event.consume();
			});
		}
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

	@Override
	public void consumeMessage(int id) {
		if (id == 0) {

			nodeGraph.getGuiMaster().removeNode(this);
			nodeGraph.update();

		} else if (id == 1) {

			nodeGraph.setActive(this);
			nodeGraph.update();
			setActive(true);
			redraw();

		} else if (id == 2) {

			nodeGraph.update();
			setActive(false);
			redraw();

		} else if (id == 3) {
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
			setOutput();

		} else if (id == 5) {
			setConstant();

		} else if (id == 6) {
			GNode node = new GNode(this);
			node.relocate(getBoundsInParent().getMinX(), getBoundsInParent().getMinY());
			nodeGraph.getGuiMaster().addNode(node);
			nodeGraph.update();
			
		} else if (id == 7) {
			if (getNodeType() == Base.PATH) {
				setPath();
			}
		}
	}

	public String getPath() {
		if (getNodeType().equals(Base.PATH)) {
			return (String) this.calcNode.get("path");
		}

		return null;
	}

	public void setPath() {
		if (getNodeType().equals(Base.PATH)) {
			File f = fileChooser.showOpenDialog(getParent().getScene().getWindow());
			if (f != null)
				this.calcNode.set("path", f.getPath());
		}
		System.out.println(getPath());
	}

	public Object getOutput() {
		if (getNodeType().equals(Base.OUTPUT)) {
			return this.calcNode.get("output");
		}
		return null;
	}

	public void setOutput() {
		if (getNodeType().equals(Base.OUTPUT)) {
			setName("Output - " + String.valueOf(this.calcNode.get("output")));
			redraw();
		}
	}

	public void setConstant() {
		if (getNodeType().equals(Base.CONSTANT)) {
			doBlur();
			TextInputDialog dialog = new TextInputDialog(getName());
			dialog.setTitle("Constant");
			dialog.setHeaderText("Set a new constant for the node.");
			dialog.setContentText("Constant: ");

			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				this.nameAddition = result.get();
				// int i = 0;
				// try {
				// i = Integer.valueOf(result.get());
				// } catch (Exception e) {
				//
				// }
				setName(this.nameAddition);
				this.calcNode.set("constant", result.get());
				redraw();
				removeBlur();
			} else {
				removeBlur();
			}
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

}
