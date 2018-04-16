package at.crimsonbit.nodesystem.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.dialog.GSubMenu;
import at.crimsonbit.nodesystem.gui.handlers.GNodeMouseHandler;
import at.crimsonbit.nodesystem.gui.layer.GLineLayer;
import at.crimsonbit.nodesystem.gui.layer.GNodeLayer;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import at.crimsonbit.nodesystem.gui.settings.GSettingsPane;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.gui.toast.Toast;
import at.crimsonbit.nodesystem.gui.toast.ToastTime;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.util.DragContext;
import at.crimsonbit.nodesystem.util.SystemUsage;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * @author NeonArtworks
 *
 */

public class GNodeGraph extends GGraphScene implements IGConsumable {

	private static final String INTERNAL_NODES = "at.crimsonbit.nodesystem.node";
	private GNodeMaster nodeMaster;
	private Group canvas;
	private GNode activeCell;
	private GNodeMouseHandler handler;
	private GPopUp popUpDialog;
	private int subMenuCount = 0;
	private int idCount = 100;
	private GPopUp graphDialog;
	private GSettingsPane settingsPane;
	private GNode toCopy;

	private GNodeLayer nodeLayer;
	private GLineLayer lineLayer;
	private HashMap<INodeType, Color> colorLookup = new HashMap<INodeType, Color>();
	private HashMap<GraphSettings, Color> nodeLookup = new HashMap<GraphSettings, Color>();
	private HashMap<GraphSettings, Object> settings = new HashMap<GraphSettings, Object>();

	private Map<INodeType, Class<? extends GNode>> nodeMap = new HashMap<INodeType, Class<? extends GNode>>();

	private final DragContext dragContext = new DragContext();
	private FileChooser fileChooser = new FileChooser();
	FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("NodeSystem files (*.nsys)", "*.nsys");
	private Rectangle selection;
	private Text nodeInfo = new Text();
	private GState state = GState.DEFAULT;
	private GSubMenu nodeMenu = new GSubMenu(0, "Add Node");
	private GSubMenu fileMenu = new GSubMenu(1, "File");

	public GNodeGraph() {

		setDefaultColorLookup();
		setNodeGraph(this);

		this.selection = new Rectangle();
		this.selection.setStroke(Color.LIGHTSKYBLUE);
		this.selection.setArcWidth(21.0);
		this.selection.setArcHeight(21.0);
		this.selection.setStrokeWidth(1);
		this.nodeMaster = new GNodeMaster(this);
		this.canvas = new Group();
		this.nodeLayer = new GNodeLayer();
		this.lineLayer = new GLineLayer();
		this.canvas.getChildren().add(nodeLayer);
		this.canvas.getChildren().add(lineLayer);
		this.canvas.getChildren().add(this.selection);
		this.getChildren().add(canvas);
		this.handler = new GNodeMouseHandler(this);
		// this.getChildren().add(this.settingsPane);
		this.canvas.getTransforms().add(getScaleTransform());
		graphDialog = new GPopUp();
		graphDialog.addItem(-1, "Node Editor", true);
		graphDialog.addSeparator(-2);
		graphDialog.addItem(nodeMenu);
		graphDialog.addItem(fileMenu);

		this.setPopUpDialog(graphDialog);

		setDefaulSettings();
	}

	/**
	 * Initializes the graph. It loads the right-click menu and adds, if
	 * defaultNodes is true, all default nodes.
	 * 
	 * @param defaultNodes
	 *            tells the graph whether it should load in the default nodes or not
	 */

	public void initGraph(boolean defaultNodes) {
		if (defaultNodes)
			getGuiMaster().registerNodes(INTERNAL_NODES);

		fillNodeList();
		loadMenus();
		addKeySupport();

	}

	public void addCustomNode(INodeType type, Class<? extends GNode> node) {
		nodeMap.put(type, node);
	}

	private void fillNodeList() {
		GNode dummy_tmp = new GNode("dummy", false);
		Set<INodeType> map = getGuiMaster().getNodeMaster().getAllNodeClasses();
		for (INodeType type : map) {
			nodeMap.put(type, dummy_tmp.getClass());
		}
	}

	public GState getState() {
		return state;
	}

	public void setState(GState state) {
		this.state = state;
	}

	public Text getNodeInfo() {
		return this.nodeInfo;
	}

	/**
	 * DEBUG ONLY!
	 */
	public void addInfo() {
		nodeInfo.setFill(Color.WHITE);
		nodeInfo.relocate(getWidth() / 2, getHeight() / 2);
		getChildren().add(nodeInfo);
		update();
	}

	public void addSetting(GraphSettings s, Object r) {
		this.settings.put(s, r);
	}

	public HashMap<GraphSettings, Object> getSettings() {
		return this.settings;
	}

	/**
	 * Adds support to select multiple nodes at once. TODO!
	 */
	public void addSelectGroupSupport() {
		setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (getActive() == null) {
					double scale = getScaleValue();
					dragContext.x = getBoundsInParent().getMinX() * scale - event.getScreenX();
					dragContext.y = getBoundsInParent().getMinY() * scale - event.getScreenY();
					setCursor(Cursor.MOVE);

					selection.setX(event.getSceneX());
					selection.setY(event.getSceneY());
				}
			}

		});

		setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				if (getActive() == null) {
					double offsetX = event.getScreenX() + dragContext.x;
					double offsetY = event.getScreenY() + dragContext.y;

					// adjust the offset in case we are zoomed
					double scale = getScaleValue();

					offsetX /= scale;
					offsetY /= scale;
					selection.setWidth(offsetX);
					selection.setHeight(offsetY);
				}
			}

		});
	}

	private void addKeySupport() {
		if (getScene() != null)
			getScene().setOnKeyPressed(onKeyPressedEventHandler);
	}

	private EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			if (getActive() != null) {

				if (event.isControlDown() && event.getCode().equals(KeyCode.C)) {
					toCopy = new GNode(getActive());
				}
				if (event.isControlDown() && event.getCode().equals(KeyCode.V) && toCopy != null) {
					if (toCopy != null) {
						toCopy = new GNode(getActive());
					}
					getGuiMaster().addNode(toCopy);
					toCopy.relocate(getActive().getBoundsInParent().getMinX(),
							getActive().getBoundsInParent().getMinY());
					setActive(toCopy);
					toCopy.toFront();
					update();
				}
				if (event.getCode().equals(KeyCode.DELETE)) {
					if (getActive() != null) {
						getGuiMaster().removeNode(getActive());
						update();
					} else if (getState().equals(GState.PORTCON)) {
						setState(GState.DEFAULT);
					}
				}
				if (event.isShiftDown() && event.getCode().equals(KeyCode.C)) {
					for (INodeType t : Constant.values())
						if (getActive().getNodeType().equals(t)) {
							getActive().setConstant();
							// update();
						}
				}
				if (event.isShiftDown() && event.getCode().equals(KeyCode.O)) {
					if (getActive().getNodeType().equals(Base.OUTPUT)) {
						getActive().setOutput();
						// update();
					}
				}
			}
			if (event.isControlDown() && event.getCode().equals(KeyCode.N)) {
				popUpDialog.show(nodeMaster.getNodeGraph(), getpX(), getpY());
			}
		}
	};

	public void setPopUpDialog(GPopUp dialog) {
		this.popUpDialog = dialog;
		addPopUpHandler(this.popUpDialog);
	}

	public void registerNodes(String path) {
		getGuiMaster().registerNodes(path);
	}

	private void loadMenus() {

		Set<INodeType> map = getGuiMaster().getNodeMaster().getAllNodeClasses();

		Map<String, GSubMenu> cache = new HashMap<>();
		for (INodeType type : map) {

			String name = type.getClass().getSimpleName();
			GSubMenu menu = cache.get(name);
			if (menu == null) {
				menu = new GSubMenu(subMenuCount++, name);
				cache.put(name, menu);
			}
			GEntry ent = new GEntry(idCount++, type.toString(), false);
			int id = Integer.valueOf(ent.getId());
			ent.setOnAction(event -> {
				consumeMessage(id, (GEntry) ent);
				event.consume();

			});
			menu.addItem(ent);

		}
		for (GSubMenu menu : cache.values()) {
			nodeMenu.addMenu(menu);

			// menuBar.addMenu(menu);
		}

		GEntry saveGraph = new GEntry(1000, "Save Graph", false);
		GEntry loadGraph = new GEntry(1001, "Load Graph", false);
		GEntry closeGraph = new GEntry(1002, "Close Graph", false);
		int id = Integer.valueOf(saveGraph.getId());
		saveGraph.setOnAction(event -> {
			consumeMessage(id, (GEntry) saveGraph);
			event.consume();

		});
		int idd = Integer.valueOf(loadGraph.getId());
		loadGraph.setOnAction(event -> {
			consumeMessage(idd, (GEntry) loadGraph);
			event.consume();

		});
		int iddd = Integer.valueOf(closeGraph.getId());
		closeGraph.setOnAction(event -> {
			consumeMessage(iddd, (GEntry) closeGraph);
			event.consume();

		});
		fileMenu.addItem(saveGraph);
		fileMenu.addItem(loadGraph);
		fileMenu.addSeparator(1003);
		fileMenu.addItem(closeGraph);

	}

	private void addPopUpHandler(GPopUp dialog) {
		this.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED, event -> {
			setCurX(event.getSceneX());
			setCurY(event.getSceneY());
			dialog.show(this, event.getScreenX(), event.getScreenY());
			event.consume();
		});

		this.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
			dialog.hide();
		});
	}

	@SuppressWarnings("static-access")
	@Override
	public void consumeMessage(int id, GEntry source) {
		if (id < 1000) {

			INodeType type = getGuiMaster().getNodeMaster().getTypeByName(source.getName());
			Class<? extends GNode> clazz = nodeMap.get(type);
			Constructor<? extends GNode> con;
			try {
				con = clazz.getConstructor(String.class, INodeType.class, boolean.class, GNodeGraph.class, double.class,
						double.class);
				GNode node = con.newInstance(source.getName(), type, true, this, getCurX(), getCurY());
				getGuiMaster().addNode(node);
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}

		}

		if (id == 1001) {
			// TODO LOADING
			onLoad();

		}
		if (id == 1000) {
			// TODO SAVING
			onSave();

		}
		if (id == 1002) {
			Stage stage = (Stage) getScene().getWindow();
			stage.close();
		}

		update();
	}

	private void onSave() {
		fileChooser.getExtensionFilters().add(extFilter);
		File f = fileChooser.showSaveDialog(getParent().getScene().getWindow());
		if (f != null)
			try {
				getGuiMaster().getNodeMaster().save(f.getPath(), true);
				Toast.makeToast("NodeSystem saved successfully!", ToastTime.TIME_SHORT);
			} catch (IOException e) {
				Toast.makeToast("Error while saving!", ToastTime.TIME_SHORT);
			}
		else
			Toast.makeToast("Error file is null!", ToastTime.TIME_SHORT);
	}

	@SuppressWarnings("static-access")
	private void onLoad() {
		fileChooser.getExtensionFilters().add(extFilter);
		File f = fileChooser.showOpenDialog(getParent().getScene().getWindow());
		if (f != null)
			try {
				getGuiMaster().setNodeMaster(getGuiMaster().getNodeMaster().load(f.getPath()));
				Toast.makeToast("NodeSystem loaded successfully!", ToastTime.TIME_SHORT);
			} catch (IOException | NoSuchNodeException e) {
				Toast.makeToast("Error while saving!", ToastTime.TIME_SHORT);
				e.printStackTrace();
			}
		else
			Toast.makeToast("Error file is null!", ToastTime.TIME_SHORT);

		GLoader.loadGUI(nodeMaster, nodeMaster.getNodeMaster(), this);

	}

	public GGraphScene getGrid() {
		return this;
	}

	public void setActive(Node n) {
		if (!(n == null))
			activeCell = (GNode) n;
		else
			activeCell = null;
		update();
		// getSettingsPane().setNode((GNode) n);
		// getSettingsPane().redraw();
	}

	public GNode getActive() {

		return this.activeCell;

	}

	public Pane getLineLayer() {
		return this.lineLayer;
	}

	public Pane getNodeLayer() {
		return this.nodeLayer;
	}

	public GNodeMaster getGuiMaster() {
		return nodeMaster;
	}

	public void update() {
		getNodeLayer().getChildren().addAll(nodeMaster.getAddedCells());
		getLineLayer().getChildren().addAll(nodeMaster.getAddedEdges());
		getNodeLayer().getChildren().removeAll(nodeMaster.getRemovedCells());
		getLineLayer().getChildren().removeAll(nodeMaster.getRemovedEdges());
		getNodeLayer().toFront();
		for (GNode cell : nodeMaster.getAddedCells()) {
			handler.addMouseHandler(cell);
		}
		getGuiMaster().attachOrphansToGraphParent(nodeMaster.getAddedCells());
		getGuiMaster().disconnectFromGraphParent(nodeMaster.getRemovedCells());
		getGuiMaster().merge();
		if (getActive() != null)
			nodeInfo.setText(SystemUsage.getRamInfo() + "\nactive node: " + getActive().getName() + ", input ports: "
					+ getActive().getInputPorts().size() + ", output ports: " + getActive().getOutputPorts().size()
					+ ", connections: " + getActive().getConnections().size());
		else
			nodeInfo.setText(SystemUsage.getRamInfo());

	}

	public double getScale() {
		return this.getScaleValue();
	}

	private void setDefaulSettings() {
		addSetting(GraphSettings.SETTING_CURVE_WIDTH, 4d);
		addSetting(GraphSettings.SETTING_CURVE_CURVE, 50d);
		addSetting(GraphSettings.COLOR_SHADOW_COLOR, 0.1d);
		addSetting(GraphSettings.SETTING_SHADOW_WIDTH, 5d);
		addSetting(GraphSettings.SETTING_SHADOW_HEIGHT, 5d);
		addSetting(GraphSettings.SETTING_SHADOW_RADIUS, 20d);

	}

	private void setDefaultColorLookup() {
		getColorLookup().put(Base.OUTPUT, Color.LIGHTBLUE);
		getColorLookup().put(Base.PATH, Color.DARKSEAGREEN);
		for (INodeType t : Constant.values())
			getColorLookup().put(t, Color.INDIANRED);

		for (INodeType t : Math.values())
			getColorLookup().put(t, Color.ORANGE);

		for (INodeType t : Calculate.values())
			getColorLookup().put(t, Color.DARKORANGE);

		for (INodeType t : Image.values())
			getColorLookup().put(t, Color.BROWN);

		for (INodeType t : ImageFilter.values())
			getColorLookup().put(t, Color.SADDLEBROWN);

		getGeneralColorLookup().put(GraphSettings.COLOR_NODE_ACTIVE, new Color(0.992, 0.647, 0.305, 1));
		getGeneralColorLookup().put(GraphSettings.COLOR_PORT_INPUT, Color.LIGHTBLUE);
		getGeneralColorLookup().put(GraphSettings.COLOR_PORT_OUTPUT, Color.LIGHTGREEN);
		getGeneralColorLookup().put(GraphSettings.COLOR_CURVE_DEFAULT, Color.CRIMSON);
		getGeneralColorLookup().put(GraphSettings.COLOR_TEXT_COLOR, Color.WHITE);
		getGeneralColorLookup().put(GraphSettings.COLOR_BACKGROUND, new Color(0.16, 0.16, 0.16, 1));
		getGeneralColorLookup().put(GraphSettings.COLOR_BACKGROUND_LINES, new Color(0.098, 0.098, 0.098, 1));
	}

	public void addNodeColorLookup(GraphSettings string, Color c) {
		this.nodeLookup.put(string, c);
	}

	public HashMap<GraphSettings, Color> getGeneralColorLookup() {
		return this.nodeLookup;
	}

	public void addColorLookup(INodeType type, Color c) {
		this.colorLookup.put(type, c);
	}

	public HashMap<INodeType, Color> getColorLookup() {
		return colorLookup;
	}

	public void setSettingsPane(GSettingsPane settingsPane2) {
		this.settingsPane = settingsPane2;

	}

	public GSettingsPane getSettingsPane() {
		return this.settingsPane;
	}

	public void clearGraph() {
		nodeMaster.clear();
		update();
	}

}