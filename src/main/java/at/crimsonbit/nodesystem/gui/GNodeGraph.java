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
import at.crimsonbit.nodesystem.node.types.MathType;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.util.GNodeMouseHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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

	private GNodeLayer nodeLayer;
	private GLineLayer lineLayer;

	private HashMap<INodeType, Color> colorLookup = new HashMap<INodeType, Color>();
	private HashMap<String, Color> nodeLookup = new HashMap<String, Color>();

	public GNodeGraph() {
		this.settingsPane = new GSettingsPane();
		this.nodeMaster = new GNodeMaster();
		this.canvas = new Group();
		this.nodeLayer = new GNodeLayer();
		this.lineLayer = new GLineLayer();
		this.canvas.getChildren().add(nodeLayer);

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

	}

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
				getGuiMaster().addNode(source.getName(), type, true, this);
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

		getNodeLayer().getChildren().addAll(nodeMaster.getAddedEdges());
		getNodeLayer().getChildren().addAll(nodeMaster.getAddedCells());

		// remove components from graph pane
		getNodeLayer().getChildren().removeAll(nodeMaster.getRemovedCells());
		getNodeLayer().getChildren().removeAll(nodeMaster.getRemovedEdges());

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

		getNodeColorLookup().put("input", Color.LIGHTBLUE);
		getNodeColorLookup().put("output", Color.LIGHTGREEN);

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