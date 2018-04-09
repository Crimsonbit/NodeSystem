package at.crimsonbit.nodesystem.gui;

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
import at.crimsonbit.nodesystem.node.types.BaseType;
import at.crimsonbit.nodesystem.node.types.CalculateType;
import at.crimsonbit.nodesystem.node.types.ImageType;
import at.crimsonbit.nodesystem.node.types.MathType;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.util.DragContext;
import at.crimsonbit.nodesystem.util.GNodeMouseHandler;
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

/**
 * 
 * @author NeonArtworks
 *
 */
@SuppressWarnings({ "restriction", "unused" })
public class GNodeGraph extends GBackground implements IGConsumable {

	private GNodeMaster nodeMaster;
	private Group canvas;
	private GNode activeCell;
	private GNodeMouseHandler handler;
	private GPopUp popUpDialog;
	private int subMenuCount = 0;
	private int subsubMenuCount = 0;
	private int idCount = 100;
	private GPopUp graphDialog;
	private GSettingsPane settingsPane;
	private GNode toCopy;

	private GNodeLayer nodeLayer;
	private GLineLayer lineLayer;

	private double curX;
	private double curY;
	private double pX;
	private double pY;

	private HashMap<INodeType, Color> colorLookup = new HashMap<INodeType, Color>();
	private HashMap<String, Color> nodeLookup = new HashMap<String, Color>();
	private HashMap<String, Object> settings = new HashMap<String, Object>();

	private final DragContext dragContext = new DragContext();

	private Rectangle selection;

	public GNodeGraph() {
		this.selection = new Rectangle();
		// this.selection.setFill(Color.TRANSPARENT);
		this.selection.setStroke(Color.LIGHTSKYBLUE);
		this.selection.setArcWidth(21.0);
		this.selection.setArcHeight(21.0);
		this.selection.setStrokeWidth(1);

		this.settingsPane = new GSettingsPane();
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

		setDefualtColorLookup();

		graphDialog = new GPopUp();
		graphDialog.addItem(-1, "Node Editor", true);
		graphDialog.addSeparator(-2);

		GSubMenu nodeMenu = new GSubMenu(0, "Add Node");
		addNodeMenus(nodeMenu);

		graphDialog.addItem(nodeMenu);

		this.setPopUpDialog(graphDialog);
		addSelectGroupSupport();
		setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				pX = event.getScreenX();
				pY = event.getScreenY();
				curX = event.getSceneX();
				curY = event.getSceneY();
			}
		});
		
		addSetting("curve_width", 4d);

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
					getGuiMaster().addNode(toCopy);
					toCopy.relocate(getActive().getBoundsInParent().getMinX(),
							getActive().getBoundsInParent().getMinY());
					update();
				}
				if (event.getCode().equals(KeyCode.DELETE)) {
					getGuiMaster().removeNode(getActive());
					update();
				}
				if (event.isShiftDown() && event.getCode().equals(KeyCode.C)) {
					if (getActive().getNodeType().equals(BaseType.CONSTANT)) {
						getActive().setConstant();
						// update();
					}
				}
				if (event.isShiftDown() && event.getCode().equals(KeyCode.O)) {
					if (getActive().getNodeType().equals(BaseType.OUTPUT)) {
						getActive().setOutput();
						// update();
					}
				}
			}
			if (event.isControlDown() && event.getCode().equals(KeyCode.N)) {
				popUpDialog.show(nodeMaster.getNodeGraph(), pX, pY);
			}
		}
	};

	public void setPopUpDialog(GPopUp dialog) {
		this.popUpDialog = dialog;
		addPopUpHandler(this.popUpDialog);
	}

	private void addNodeMenus(GSubMenu nodeMenu) {
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
		for (GSubMenu menu : cache.values())
			nodeMenu.addMenu(menu);
		/*
		 * for (String s : map.keySet()) { GSubMenu baseNodeMenu = new
		 * GSubMenu(subMenuCount++, s); baseNodeMenu.addItem(-1, s, true); for
		 * (INodeType t : map.values()) { GEntry ent = new GEntry(idCount++, "hi",
		 * false); baseNodeMenu.addItem(ent); int id = Integer.valueOf(ent.getId());
		 * ent.setOnAction(event -> { consumeMessage(id, (GEntry) ent); event.consume();
		 * 
		 * }); }
		 */
		// nodeMenu.addMenu(baseNodeMenu);
		// }

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
			curX = event.getSceneX();
			curY = event.getSceneY();
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
		// for(int i = 0; i<idCount;i++) {
		// if(i == id) {
		Set<INodeType> map = getGuiMaster().getNodeMaster().getAllNodeClasses();
		for (INodeType type : map) {
			if (source.getName().toUpperCase() == type.toString())
				getGuiMaster().addNode(source.getName(), type, true, this, curX, curY);
		}

		// }
		// }

		update();
	}

	public GBackground getGrid() {
		return this;
	}

	public void setActive(Node n) {
		activeCell = (GNode) n;
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

	}

	public double getScale() {
		return this.getScaleValue();
	}

	private void setDefualtColorLookup() {
		getColorLookup().put(BaseType.OUTPUT, Color.LIGHTBLUE);
		getColorLookup().put(BaseType.CONSTANT, Color.RED);
		for (INodeType t : MathType.values())
			getColorLookup().put(t, Color.ORANGE);
		for (INodeType t : CalculateType.values()) {
			getColorLookup().put(t, Color.DARKORANGE);
		}
		for (INodeType t : ImageType.values())
			getColorLookup().put(t, Color.BROWN);

		getNodeColorLookup().put("input", Color.LIGHTBLUE);
		getNodeColorLookup().put("output", Color.LIGHTGREEN);
		getNodeColorLookup().put("curve", Color.CRIMSON);
		getNodeColorLookup().put("text", Color.WHITE);
	}

	public void addNodeColorLookup(String string, Color c) {
		this.nodeLookup.put(string, c);
	}

	public HashMap<String, Color> getNodeColorLookup() {
		return this.nodeLookup;
	}

	public void addColorLookup(INodeType type, Color c) {
		this.colorLookup.put(type, c);
	}

	public HashMap<INodeType, Color> getColorLookup() {
		return colorLookup;
	}
}