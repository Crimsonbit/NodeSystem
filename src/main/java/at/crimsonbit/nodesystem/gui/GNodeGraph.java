package at.crimsonbit.nodesystem.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.dialog.GSubMenu;
import at.crimsonbit.nodesystem.gui.layer.GLineLayer;
import at.crimsonbit.nodesystem.gui.layer.GNodeLayer;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import at.crimsonbit.nodesystem.gui.settings.GSettingsPane;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.util.DragContext;
import at.crimsonbit.nodesystem.util.GNodeMouseHandler;
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
import javafx.stage.Stage;

/**
 * 
 * @author NeonArtworks
 *
 */

public class GNodeGraph extends GGraphScene implements IGConsumable {

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
	private Pane infoLayer = new Pane();
	private HashMap<INodeType, Color> colorLookup = new HashMap<INodeType, Color>();
	private HashMap<String, Color> nodeLookup = new HashMap<String, Color>();
	private HashMap<String, Object> settings = new HashMap<String, Object>();
	private Map<INodeType, Class<? extends GNode>> nodeMap = new HashMap<INodeType, Class<? extends GNode>>();

	private final DragContext dragContext = new DragContext();

	private Rectangle selection;
	private Text nodeInfo = new Text();
	private GState state = GState.DEFAULT;
	private GSubMenu nodeMenu = new GSubMenu(0, "Add Node");
	private GSubMenu fileMenu = new GSubMenu(1, "File");

	public GNodeGraph() {

		setDefualtColorLookup();
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
		this.canvas.getChildren().add(infoLayer);
		this.getChildren().add(canvas);
		this.handler = new GNodeMouseHandler(this);
		// this.getChildren().add(this.settingsPane);

		graphDialog = new GPopUp();
		graphDialog.addItem(-1, "Node Editor", true);
		graphDialog.addSeparator(-2);
		graphDialog.addItem(nodeMenu);
		graphDialog.addItem(fileMenu);

		this.setPopUpDialog(graphDialog);
		addSetting("curve_width", 4d);

	}

	public void initGraph() {
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

	public void addInfo() {
		nodeInfo.setFill(Color.WHITE);
		nodeInfo.relocate(getWidth() / 2, getHeight() / 2);
		infoLayer.getChildren().add(nodeInfo);
		update();
	}

	public void addSetting(String s, Object r) {
		this.settings.put(s, r);
	}

	public HashMap<String, Object> getSettings() {
		return this.settings;
	}

	/**
	 * Adds support to select multiple nodes at once.
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

	public void addKeySupport() {
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
					if (getActive().getNodeType().equals(Base.CONSTANT)) {
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

	public void loadMenus() {

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

	/*
	 * private void addSubeMenuHandlers() { for (int i = 0; i < subMenuCount; i++) {
	 * GSubMenu menu = (GSubMenu) this.popUpDialog.getItems().get(2 + i); // for
	 * (int j = 0; i < subsubMenuCount; j++) { GSubMenu subMenu = (GSubMenu)
	 * menu.getItems().get(0); if (subMenu instanceof GSubMenu) {
	 * 
	 * for (MenuItem item : menu.getItems()) { if (!(item instanceof GSubMenu)) {
	 * int id = Integer.valueOf(item.getId()); item.setOnAction(event -> {
	 * consumeMessage(id, (GEntry) item); event.consume();
	 * 
	 * }); } } // } } for (MenuItem item : menu.getItems()) { if (!(item instanceof
	 * GSubMenu)) { int id = Integer.valueOf(item.getId()); item.setOnAction(event
	 * -> { consumeMessage(id, (GEntry) item); event.consume();
	 * 
	 * }); } } } }
	 */
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

		// addSubeMenuHandlers();

		/*
		 * for (MenuItem item : this.popUpDialog.getItems()) { int id =
		 * Integer.valueOf(item.getId()); item.setOnAction(event -> { //
		 * System.out.println(id); consumeMessage(id); event.consume(); });
		 * 
		 * }
		 */
	}

	@Override
	public void consumeMessage(int id, GEntry source) {
		if (id < 1000) {
			Set<INodeType> map = getGuiMaster().getNodeMaster().getAllNodeClasses();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (id == 1001) {
			System.out.println("loading!");
		}
		if (id == 1000) {
			System.out.println("saving!");
		}
		if (id == 1002) {
			Stage stage = (Stage) getScene().getWindow();
			stage.close();
		}

		update();
	}

	public GGraphScene getGrid() {
		return this;
	}

	public void setActive(Node n) {
		if (!(n == null))
			activeCell = (GNode) n;
		else
			activeCell = null;
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

	public void beginUpdate() {
	}

	public void update() {

		// add components to graph pane

		getNodeLayer().getChildren().addAll(nodeMaster.getAddedCells());
		getLineLayer().getChildren().addAll(nodeMaster.getAddedEdges());
		// remove components from graph pane
		getNodeLayer().getChildren().removeAll(nodeMaster.getRemovedCells());
		getLineLayer().getChildren().removeAll(nodeMaster.getRemovedEdges());
		getNodeLayer().toFront();
		// enable dragging of cells
		for (GNode cell : nodeMaster.getAddedCells()) {
			handler.addMouseHandler(cell);
		}

		// every cell must have a parent, if it doesn't, then the graphParent is
		// the parent
		getGuiMaster().attachOrphansToGraphParent(nodeMaster.getAddedCells());

		// remove reference to graphParent
		getGuiMaster().disconnectFromGraphParent(nodeMaster.getRemovedCells());

		// merge added & removed cells with all cells
		getGuiMaster().merge();

		/*
		 * if (getActive() != null) { if (getActive().getNodeType().equals(Base.OUTPUT)
		 * && getActive().getOutput() != null) {
		 * nodeInfo.setText(SystemUsage.getRamInfo() + ", Current Output Value: " +
		 * getActive().getOutput()); } } else
		 */
		nodeInfo.setText(SystemUsage.getRamInfo());

	}

	public double getScale() {
		return this.getScaleValue();
	}

	private void setDefualtColorLookup() {
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

		getGeneralColorLookup().put("active", new Color(0.992, 0.647, 0.305, 1));
		getGeneralColorLookup().put("input", Color.LIGHTBLUE);
		getGeneralColorLookup().put("output", Color.LIGHTGREEN);
		getGeneralColorLookup().put("curve", Color.CRIMSON);
		getGeneralColorLookup().put("text", Color.WHITE);
		getGeneralColorLookup().put("background", new Color(0.16, 0.16, 0.16, 1));
		getGeneralColorLookup().put("line_color", new Color(0.992, 0.647, 0.305, 1));
	}

	public void addNodeColorLookup(String string, Color c) {
		this.nodeLookup.put(string, c);
	}

	public HashMap<String, Color> getGeneralColorLookup() {
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
}