package at.crimsonbit.nodesystem.gui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.logging.Level;

import at.crimsonbit.nodesystem.gui.dialog.GEntry;
import at.crimsonbit.nodesystem.gui.dialog.GPopUp;
import at.crimsonbit.nodesystem.gui.dialog.GSubMenu;
import at.crimsonbit.nodesystem.gui.handlers.GNodeMouseHandler;
import at.crimsonbit.nodesystem.gui.layer.GLineLayer;
import at.crimsonbit.nodesystem.gui.layer.GNodeLayer;
import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.gui.node.IGConsumable;
import at.crimsonbit.nodesystem.gui.settings.GraphSettings;
import at.crimsonbit.nodesystem.gui.widget.searchbar.GSearchBar;
import at.crimsonbit.nodesystem.gui.widget.toast.JFXToast;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastPosition;
import at.crimsonbit.nodesystem.gui.widget.toast.ToastTime;
import at.crimsonbit.nodesystem.node.arduino.ArduinoNodeClass;
import at.crimsonbit.nodesystem.node.base.OutputNodeClass;
import at.crimsonbit.nodesystem.node.base.PathNodeClass;
import at.crimsonbit.nodesystem.node.constant.ConstantNodeClass;
import at.crimsonbit.nodesystem.node.image.ImageNodeClass;
import at.crimsonbit.nodesystem.node.types.Arduino;
import at.crimsonbit.nodesystem.node.types.ArduinoPinMode;
import at.crimsonbit.nodesystem.node.types.Base;
import at.crimsonbit.nodesystem.node.types.Calculate;
import at.crimsonbit.nodesystem.node.types.Constant;
import at.crimsonbit.nodesystem.node.types.Image;
import at.crimsonbit.nodesystem.node.types.ImageFilter;
import at.crimsonbit.nodesystem.node.types.ImageHelper;
import at.crimsonbit.nodesystem.node.types.Math;
import at.crimsonbit.nodesystem.node.types.Noise;
import at.crimsonbit.nodesystem.node.types.Terrain;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.nodebackend.api.NodeMaster;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.util.Tuple;
import at.crimsonbit.nodesystem.util.DragContext;
import at.crimsonbit.nodesystem.util.SystemUsage;
import at.crimsonbit.nodesystem.util.logger.SystemLogger;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Light.Point;
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
 * <h1>GNodeGraph extends {@link GGraphScene} implements
 * {@link IGConsumable}</h1>
 * <p>
 * The GnodeGraph class represents the graphical layer of the node-system. A lot
 * of internal stuff is handled in this class.
 * </p>
 * 
 * @author Florian Wagner
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
	private Scene sc;
	private GNodeLayer nodeLayer;
	private GLineLayer lineLayer;
	private HashMap<INodeType, Color> colorLookup = new HashMap<INodeType, Color>();
	private HashMap<GraphSettings, Color> nodeLookup = new HashMap<GraphSettings, Color>();
	private HashMap<GraphSettings, Object> settings = new HashMap<GraphSettings, Object>();
	private Set<GNode> selectedNodesGroup = new HashSet<GNode>();

	private GSearchBar bar = new GSearchBar();
	private final Map<INodeType, Class<? extends GNode>> nodeMap = new HashMap<INodeType, Class<? extends GNode>>();
	private GClipboard clipboard;
	private final DragContext dragContext = new DragContext();
	private FileChooser fileChooser = new FileChooser();
	private FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("NodeSystem files (*.nsys)",
			"*.nsys");
	private Rectangle selection;
	private Text nodeInfo = new Text();
	private GState state = GState.DEFAULT;
	private GSubMenu nodeMenu = new GSubMenu(0, "Add Node");
	private GSubMenu fileMenu = new GSubMenu(1, "File");
	private GLogPane logPane;
	private InnerShadow innerShadow;

	public GNodeGraph() {
		try {
			log = SystemLogger.getLogger(logPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log(Level.INFO, "Setting up NodeGraph...");

		setDefaultColorLookup();
		setNodeGraph(this);

		this.selection = new Rectangle();
		this.selection.setStroke(Color.LIGHTSKYBLUE);
		this.selection.setArcWidth(21.0);
		this.selection.setArcHeight(21.0);
		this.selection.setStrokeWidth(1);
		this.selection.setFill(Color.TRANSPARENT);
		this.selection.getStrokeDashArray().add(10.0);
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
		log(Level.INFO, "NodeGraph set-up successfully!");

		innerShadow = new InnerShadow();

		// Setting the offset values of the inner shadow
		innerShadow.setOffsetX(4);
		innerShadow.setOffsetY(4);

		innerShadow.setBlurType(BlurType.GAUSSIAN);
		double col = (double) getSettings().get(GraphSettings.COLOR_SHADOW_COLOR);
		innerShadow.setColor(new Color(col, col, col, 1));
		innerShadow.setWidth((double) getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
		innerShadow.setHeight((double) getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
		innerShadow.setOffsetX((double) getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
		innerShadow.setOffsetY((double) getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
		innerShadow.setRadius((double) getSettings().get(GraphSettings.SETTING_SHADOW_RADIUS));
		clipboard = GClipboard.getClipboard(this);
		setEffect(innerShadow);
		addSelectGroupSupport();
	}

	public GNodeGraph(GLogPane logPane) {
		setLogPane(logPane);
		try {
			log = SystemLogger.getLogger(logPane);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log(Level.INFO, "Setting up NodeGraph...");

		setDefaultColorLookup();
		setNodeGraph(this);

		this.selection = new Rectangle();
		this.selection.setStroke(Color.LIGHTSKYBLUE);
		this.selection.setArcWidth(21.0);
		this.selection.setArcHeight(21.0);
		this.selection.setStrokeWidth(1);
		this.selection.setFill(Color.TRANSPARENT);
		this.selection.getStrokeDashArray().add(10.0);
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
		log(Level.INFO, "NodeGraph set-up successfully!");

		innerShadow = new InnerShadow();

		// Setting the offset values of the inner shadow
		innerShadow.setOffsetX(4);
		innerShadow.setOffsetY(4);

		innerShadow.setBlurType(BlurType.GAUSSIAN);
		double col = (double) getSettings().get(GraphSettings.COLOR_SHADOW_COLOR);
		innerShadow.setColor(new Color(col, col, col, 1));
		innerShadow.setWidth((double) getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
		innerShadow.setHeight((double) getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
		innerShadow.setOffsetX((double) getSettings().get(GraphSettings.SETTING_SHADOW_WIDTH));
		innerShadow.setOffsetY((double) getSettings().get(GraphSettings.SETTING_SHADOW_HEIGHT));
		innerShadow.setRadius((double) getSettings().get(GraphSettings.SETTING_SHADOW_RADIUS));
		clipboard = GClipboard.getClipboard(this);
		setEffect(innerShadow);
		addSelectGroupSupport();
	}

	public GLogPane getLogPane() {
		return logPane;
	}

	public void setLogPane(GLogPane logPane) {
		this.logPane = logPane;
	}

	public Set<GNode> getSelectedNodesGroup() {
		return selectedNodesGroup;
	}

	public void setSelectedNodesGroup(Set<GNode> selectedNodesGroup) {
		this.selectedNodesGroup = selectedNodesGroup;
	}

	/**
	 * <h1>public void initGraph({@link Boolean}).</h1>
	 * <hr>
	 * <p>
	 * Initializes the graph. This method must be called after the graph was added
	 * to a scene. Otherwise the NodeSystem will throw an error when starting.
	 *
	 * </p>
	 * 
	 * @param defaultNodes
	 *            tells the graph whether it should load in the default nodes or not
	 */

	public void initGraph(boolean defaultNodes) {
		log(Level.INFO, "Initializing NodeGraph...");
		if (defaultNodes) {
			log(Level.INFO, "Loading internal nodes...");
			getGuiMaster().registerNodes(INTERNAL_NODES);
		}
		fillNodeList();
		loadMenus();
		addKeySupport();

		log(Level.INFO, "Added Menus and Key-support!");
		addCustomNode(Base.OUTPUT, OutputNodeClass.class);
		addCustomNode(Base.PATH, PathNodeClass.class);

		for (INodeType t : Arduino.values()) {
			addCustomNode(t, ArduinoNodeClass.class);
		}

		for (INodeType t : Image.values()) {
			addCustomNode(t, ImageNodeClass.class);
		}

		for (INodeType t : Constant.values()) {
			addCustomNode(t, ConstantNodeClass.class);
		}
		log(Level.INFO, "Added default custom node classes!");
	}

	/**
	 * 
	 * <h1>public void addCustomDialogEntry({@link GSubMenu},
	 * {@link BiConsumer})</h1>
	 * 
	 * <p>
	 * This method can be used to add custom menu entries to the node-graph.
	 * </p>
	 * 
	 * @param menu
	 *            the menu you want to add.
	 * @param func
	 *            a {@link BiConsumer} which has an {@link Integer} and an
	 *            {@link GEntry}. This is the consumer that gets called when you
	 *            click on the item. For more information see the
	 *            CustomGraphDialogs.java example!
	 */
	public void addCustomDialogEntry(GSubMenu menu, BiConsumer<Integer, GEntry> func) {
		graphDialog.addItem(menu);
		for (MenuItem ent : menu.getItems()) {
			int id = Integer.valueOf(ent.getId());
			ent.setOnAction(event -> {
				func.accept(id, (GEntry) ent);
				event.consume();

			});
		}
	}

	/**
	 * <h1>public void addCustomNode({@link INodeType}, {@link Class}).</h1>
	 * <hr>
	 * <p>
	 * With this method it is possible to add custom node-classes to specific
	 * node-types. For more information on how to add a custom node-type and class,
	 * please see the example.
	 * </p>
	 * 
	 * @param type
	 *            the node you want to add your custom class.
	 * @param clazz
	 *            the {@link Class} of your custom node-class you want to use.
	 */
	public void addCustomNode(INodeType type, Class<? extends GNode> clazz) {
		getNodeMap().put(type, clazz);
	}

	public void addCustomNode(INodeType[] values, Class<? extends GNode> clazz) {
		for (INodeType t : values) {
			getNodeMap().put(t, clazz);
		}

	}

	private void fillNodeList() {
		GNode dummy_tmp = new GNode("dummy", false);
		Set<INodeType> map = getGuiMaster().getNodeMaster().getAllNodeClasses();
		for (INodeType type : map) {
			getNodeMap().put(type, dummy_tmp.getClass());
		}
	}

	/**
	 * <h1>public {@link GState} getState()</h1>
	 * <p>
	 * Returns the current state of the graph. Currently only DEFUALT and PORTCON
	 * are available although only DEFAULT is being used!
	 * </p>
	 * 
	 * @return the current state of the graph.
	 */
	public GState getState() {
		return state;
	}

	public void updateColors() {
		init();
	}

	/**
	 * <h1>public void setState({@link GState}).</h1>
	 * <p>
	 * 
	 * Sets the current {@link GState} of the graph. Currently only DEFUALT and
	 * PORTCON are available although only DEFAULT is being used!
	 * </p>
	 * 
	 * @param state
	 *            the state you want to set the graph to
	 */
	public void setState(GState state) {
		this.state = state;
	}

	/**
	 * <h1>public {@link Text} getNodeInfoObject()</h1>
	 * <p>
	 * Returns the {@link Text} object which holds additional information
	 * </p>
	 * 
	 * @return a text containing additional information
	 */
	public Text getNodeInfoObject() {
		return this.nodeInfo;
	}

	/**
	 * <h1>public void addInfo().</h1>
	 * <hr>
	 * <p>
	 * This method adds optional information to the screen. The {@link Text} object
	 * used for the information can be retrieved by using the method getNodeInfo().
	 * </p>
	 */
	public void addInfo() {
		nodeInfo.setFill(Color.WHITE);
		nodeInfo.relocate(getWidth() / 2, getHeight() / 2);
		getChildren().add(nodeInfo);
		update();
	}

	/**
	 * <h1>public void addSetting({@link GraphSettings}, {@link Object})</h1>
	 * <hr>
	 * <p>
	 * Using this method you can change a almost every setting, which is not
	 * hardcoded into the nodesystem. To see all settings please have a look at
	 * {@link GraphSettings}.
	 * </p>
	 * 
	 * @param s
	 *            the setting you want to change
	 * @param r
	 *            the value, please keep in mind, that setting settings to random
	 *            stuff can break the nodesystem and it can throw unexpected errors.
	 */
	public void addSetting(GraphSettings s, Object r) {
		this.settings.put(s, r);
		init();
	}

	/**
	 * <h1>public {@link HashMap}<{@link GraphSettings}, {@link Object}>
	 * getSettings()</h1>
	 * <p>
	 * Returns the HashMap containing all the settings for the graph and their
	 * corresponding values.
	 * </p>
	 * 
	 * @return the settings of the graph
	 */
	public HashMap<GraphSettings, Object> getSettings() {
		return this.settings;
	}

	/**
	 * <h1>public void addSelectGroupSupport()</h1>
	 * <p>
	 * Adds support to select multiple nodes at once. TODO: Need to fix issue when
	 * zooming
	 * </p>
	 */
	protected void addSelectGroupSupport() {

		final Point anchor = new Point();

		setOnMousePressed(event -> {
			if (!event.isSecondaryButtonDown()) {
				selectedNodesGroup.clear();
				anchor.setX(event.getX());
				anchor.setY(event.getY());
				selection.setX(event.getX() / getScaleValue());
				selection.setY(event.getY() / getScaleValue());
			}
		});

		setOnMouseDragged(event -> {
			if (getState().equals(GState.DEFAULT)) {
				if (!event.isSecondaryButtonDown()) {
					selection.setWidth(java.lang.Math.abs(event.getX() - anchor.getX()));
					selection.setHeight(java.lang.Math.abs(event.getY() - anchor.getY()));
					selection.setX(java.lang.Math.min(anchor.getX(), event.getX()));
					selection.setY(java.lang.Math.min(anchor.getY(), event.getY()));
				}
			}
		});

		setOnMouseReleased(event -> {
			// Do what you want with selection's properties here
			// System.out.printf("X: %.2f, Y: %.2f, Width: %.2f, Height: %.2f%n",
			// selection.getX(), selection.getY(),
			// selection.getWidth(), selection.getHeight());

			/**
			 * Check if nodes are in bounds.
			 */
			for (GNode n : getGuiMaster().getAllCells()) {
				if ((n.getLayoutX() > selection.getX() && n.getLayoutX() < selection.getWidth())
						&& (n.getLayoutY() > selection.getY() && n.getLayoutY() < selection.getHeight())) {
					// System.out.println(n);
					n.setActive(true);
					n.redraw();
					selectedNodesGroup.add(n);
				}
			}

			selection.setWidth(0);
			selection.setHeight(0);
		});

		/*
		 * setOnMouseClicked(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { if (getActive() == null) {
		 * selection.setWidth(0); selection.setHeight(0); selection.setArcWidth(0.0);
		 * selection.setArcHeight(0.0); selection.setStrokeWidth(0); } }
		 * 
		 * });
		 * 
		 * setOnMousePressed(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { // if (getActive() == null)
		 * { selection.setArcWidth(21.0); selection.setArcHeight(21.0);
		 * selection.setStrokeWidth(1); double scale = getScaleValue(); dragContext.x =
		 * getBoundsInParent().getMinX() * scale - event.getScreenX(); dragContext.y =
		 * getBoundsInParent().getMinY() * scale - event.getScreenY();
		 * setCursor(Cursor.MOVE); double x = event.getSceneX() / getScaleValue();
		 * double y = event.getSceneY() / getScaleValue();
		 * 
		 * selection.setX(x); selection.setY(y); // } }
		 * 
		 * });
		 * 
		 * setOnMouseDragged(new EventHandler<MouseEvent>() {
		 * 
		 * @Override public void handle(MouseEvent event) { // TODO Auto-generated
		 * method stub // if (getActive() == null) { double offsetX =
		 * (event.getScreenX() + dragContext.x); double offsetY = (event.getScreenY() +
		 * dragContext.y);
		 * 
		 * // adjust the offset in case we are zoomed double scale = getScaleValue();
		 * 
		 * offsetX /= scale; offsetY /= scale;
		 * 
		 * selection.setWidth(offsetX); selection.setHeight(offsetY); // } }
		 * 
		 * });
		 */
	}

	private void addKeySupport() {
		if (getScene() != null)
			getScene().setOnKeyPressed(onKeyPressedEventHandler);
	}

	private EventHandler<KeyEvent> onKeyPressedEventHandler = new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {

			/*
			 * Opens the search bar
			 */
			if (event.isControlDown() && event.getCode().equals(KeyCode.SPACE)) {
				if (!bar.isOpen())
					bar.search((Stage) getScene().getWindow(), GNodeGraph.this);
			}

			if (getActive() != null || getSelectedNodesGroup().size() > 0) {

				/**
				 * Rename node
				 */

				if (event.getCode().equals(KeyCode.F2) && !(getSelectedNodesGroup().size() > 0)) {
					doBlur();
					TextInputDialog dialog = new TextInputDialog(getActive().getName());
					dialog.setTitle("Name");
					dialog.setHeaderText("Set a new name for the node.");
					dialog.setContentText("Name: ");
					Optional<String> result = dialog.showAndWait();
					if (result.isPresent()) {
						getActive().setName(result.get());
						removeBlur();
					} else {
						removeBlur();
					}

					getActive().redraw();
				}

				/*
				 * Copies the active node onto the clipboard (clipboard is for GNodes only!)
				 */
				if (event.isControlDown() && event.getCode().equals(KeyCode.C)) {
					if (getSelectedNodesGroup().size() > 0) {
						clipboard.copy(getSelectedNodesGroup());
						log(Level.INFO,
								"Copied node group of size: " + getSelectedNodesGroup().size() + " to the clpboard!");

					} else {
						clipboard.copy(getActive());
						log(Level.INFO, "Node: " + getActive().getName() + " copied to clipboard!");
					}
				}

				/**
				 * Pastes from the clipboard
				 */
				if (event.isControlDown() && event.getCode().equals(KeyCode.V)) {
					if (getSelectedNodesGroup().size() > 0) {
						clipboard.pasteNodeGroup();
						log(Level.INFO,
								"Pasted node group of size: " + getSelectedNodesGroup().size() + " to the graph!");
						getSelectedNodesGroup().clear();
					} else {
						clipboard.paste();
						log(Level.INFO, "Node: " + getActive().getName() + " pasted from clipboard!");
					}
				}

				/**
				 * Removes the active node
				 */
				if (event.getCode().equals(KeyCode.DELETE)) {
					if (getSelectedNodesGroup().size() > 0) {
						for (GNode n : getSelectedNodesGroup()) {
							getGuiMaster().removeNode(n);
							update();
							log(Level.INFO, "Deleted a total of " + getSelectedNodesGroup().size() + " nodes!");
						}
						getSelectedNodesGroup().clear();
					} else {
						if (getActive() != null) {
							getGuiMaster().removeNode(getActive());
							update();
						} else if (getState().equals(GState.PORTCON)) {
							setState(GState.DEFAULT);
						}
						log(Level.INFO, "Node: " + getActive().getName() + " was deleted from the graph!");
					}

				}

				/**
				 * Sets the constant
				 */
				if (event.isShiftDown() && event.getCode().equals(KeyCode.C) && !(getSelectedNodesGroup().size() > 0)) {
					for (INodeType t : Constant.values())
						if (getActive().getNodeType().equals(t)) {
							((ConstantNodeClass) getActive()).setConstant();
							// update();
						}
				}
				/**
				 * calculates the output
				 */
				if (event.isShiftDown() && event.getCode().equals(KeyCode.O) && !(getSelectedNodesGroup().size() > 0)) {
					if (getActive().getNodeType().equals(Base.OUTPUT)) {
						((OutputNodeClass) getActive()).setOutput();

						// update();
					}
				}
			}

			/**
			 * Save the graph
			 */
			if (event.isControlDown() && event.getCode().equals(KeyCode.S)) {
				onSave();

			}
			/**
			 * Load the graph
			 */
			if (event.isControlDown() && event.getCode().equals(KeyCode.L)) {
				onLoad();

			}
			/**
			 * Show the pop up menu
			 */
			if (event.isControlDown() && event.getCode().equals(KeyCode.X)) {
				popUpDialog.show(nodeMaster.getNodeGraph(), getpX(), getpY());
			}
		}
	};

	/**
	 * <h1>public void setPopUpDialog({@link GPopUp})</h1>
	 * <p>
	 * Sets the rightclick-dialog of the graph. It is advised to not override the
	 * current dialog! Currently it is not possible to add custom rightclick
	 * behavior to the node-graph.
	 * </p>
	 * 
	 * @param dialog
	 *            the {@link GPopUp} dialog
	 */
	public void setPopUpDialog(GPopUp dialog) {
		this.popUpDialog = dialog;
		addPopUpHandler(this.popUpDialog);
	}

	/**
	 * <h1>public void registerNodes({@link String}).</h1>
	 * <hr>
	 * <p>
	 * This method registers all nodes within the package given by path. The path is
	 * a {@link String} that has to be the full package. The method does search all
	 * sub-packages automatically! So it is advised to specify one package where you
	 * have all your nodes and separate them by using sub-packages.
	 * </p>
	 * 
	 * @param pckg
	 *            the packaged represented by a {@link String}.
	 */
	public void registerNodes(String pckg) {
		log(Level.INFO, "Registering custom nodes...");
		getGuiMaster().registerNodes(pckg);

	}

	private void loadMenus() {
		log(Level.INFO, "Loading menus....");
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
			log(Level.INFO, "Adding sub-menu: " + menu.toString());
			menu.addItem(ent);

		}
		for (GSubMenu menu : cache.values()) {
			nodeMenu.addMenu(menu);
			log(Level.INFO, "Adding menu: " + menu.toString());
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
		log(Level.INFO, "Done loading menus!");
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

	public GClipboard getClipBoard() {
		return this.clipboard;
	}

	public void doBlur() {
		BoxBlur blur = new BoxBlur(3, 3, 3);
		getParent().setEffect(blur);
		setEffect(blur);
	}

	public void removeBlur() {
		getParent().setEffect(null);
		setEffect(null);
	}

	@SuppressWarnings("static-access")
	@Override
	public void consumeMessage(int id, GEntry source) {

		if (id < 1000) {
			INodeType type = getGuiMaster().getNodeMaster().getTypeByName(source.getName());
			Class<? extends GNode> clazz = getNodeMap().get(type);
			Constructor<? extends GNode> con;
			try {
				con = clazz.getConstructor(String.class, INodeType.class, boolean.class, GNodeGraph.class, double.class,
						double.class);
				GNode node = con.newInstance(source.getName(), type, true, this, getCurX(), getCurY());
				getGuiMaster().addNode(node);
				log(Level.INFO, "Node: " + node.getName() + " was added to the graph!");
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		if (id == 1000) {
			onSave();
		}

		if (id == 1001) {
			onLoad();
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
				JFXToast.makeToast((Stage) getScene().getWindow(), "NodeSystem saved successfully!",
						ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				log(Level.FINE, "NodeSystem saved successfully!");
			} catch (IOException e) {
				JFXToast.makeToast((Stage) getScene().getWindow(), "Error while saving!", ToastTime.TIME_SHORT,
						ToastPosition.BOTTOM);
				e.printStackTrace();
				log(Level.SEVERE, "Error while saving!");
			}
		else {
			JFXToast.makeToast((Stage) getScene().getWindow(), "Error file is null!", ToastTime.TIME_SHORT,
					ToastPosition.BOTTOM);

			log(Level.SEVERE, "Error file is null!");
		}
	}

	private void onLoad() {
		fileChooser.getExtensionFilters().add(extFilter);
		File f = fileChooser.showOpenDialog(getParent().getScene().getWindow());
		if (f == null) // if f == null -> No File was selected, do not do anything
			return;
		Path p = f.toPath();
		load(p);
		// GLoader.loadGUI(nodeMaster, nodeMaster.getNodeMaster(), this);

	}

	private void load(Path f) {
		if (f != null)
			try {
				boolean ret = rebuildNodeGraph(f);

				if (ret) {
					log(Level.FINE, "NodeSystem loaded successfully!");
					JFXToast.makeToast((Stage) getScene().getWindow(), "NodeSystem loaded successfully!",
							ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
				}
			} catch (Exception e) {
				JFXToast.makeToast((Stage) getScene().getWindow(), "Error while loading!", ToastTime.TIME_SHORT,
						ToastPosition.BOTTOM);
				e.printStackTrace();
				log(Level.SEVERE, "Error while loading!");
			}
		else {
			JFXToast.makeToast((Stage) getScene().getWindow(), "Error: No file selected!", ToastTime.TIME_SHORT,
					ToastPosition.BOTTOM);
			log(Level.SEVERE, "Error: No file selected!");
		}
	}

	/**
	 * <h1>public void loadGraphFromFile({@link File})</h1>
	 * <p>
	 * This method attempts to load an existing graph from a file. It is always
	 * advised to make file-checks yourself before attempting to load <br>
	 * This method has to be called AFTER the window was already created, meaning
	 * after primaryStage.show() -> see LoadExistingGraph.java example!
	 * </p>
	 * 
	 * @param graph_file
	 *            the graph file you want to load
	 */
	public void loadGraphFromFile(Path graph_file) {
		if (graph_file != null)
			load(graph_file);
	}

	private boolean rebuildNodeGraph(Path path)
			throws IOException, NoSuchNodeException, NoSuchMethodException, SecurityException, InstantiationException,
			IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		clearGraph();
		getGuiMaster().getNodeMaster().clear();
		getGuiMaster().clear();
		Tuple<NodeMaster, String> data = NodeMaster.load(path);
		if (data.a == null) {
			JFXToast.makeToast((Stage) getScene().getWindow(), data.b, ToastTime.TIME_SHORT, ToastPosition.BOTTOM);
			log(Level.SEVERE, data.b);
			return false;
		}
		getGuiMaster().setNodeMaster(data.a);
		getGuiMaster().rebuild(getGuiMaster().getNodeMaster());
		return true;
	}

	/**
	 * <h1>public {@link GGraphScene} getGrid()</h1>
	 * <p>
	 * Returns the {@link GGraphScene} of the dialog. Remember, the
	 * {@link GNodeGraph} extends the {@link GGraphScene}. It will only return a
	 * casted version of the current {@link GNodeGraph}!
	 * </p>
	 * 
	 * @return the {@link GGraphScene} of this graph
	 */
	public GGraphScene getGraphScene() {
		return this;
	}

	/**
	 * <h1>Internal function, strongly advised to not use it!</h1>
	 * 
	 * <h1>public void setActive({@linkplain GNode})</h1>
	 * <p>
	 * Sets the current active node. This is needed for a lot of things internally
	 * </p>
	 * 
	 * @param n
	 *            the node that should be set to active.
	 */
	public void setActive(GNode n) {
		if (!(n == null))
			activeCell = (GNode) n;
		else
			activeCell = null;
		update();
		// getSettingsPane().setNode((GNode) n);
		// getSettingsPane().redraw();
	}

	/**
	 * <h1>public {@link GNode} getActive()</h1>
	 * 
	 * <p>
	 * Gets the currently active {@link GNode}.
	 * </p>
	 * 
	 * @return the currently active node.
	 */
	public GNode getActive() {

		return this.activeCell;

	}

	/**
	 * <h1>public {@link Pane} getLineLayer()</h1>
	 * <p>
	 * Returns the layer on which the connections between the nodes are drawn
	 * </p>
	 * 
	 * @return the connection layer
	 */
	public Pane getLineLayer() {
		return this.lineLayer;
	}

	/**
	 * <h1>public {@link Pane} getNodeLayer()</h1>
	 * <p>
	 * Returns the layer on which the Nodes are drawn
	 * </p>
	 * 
	 * @return the node layer
	 */
	public Pane getNodeLayer() {
		return this.nodeLayer;
	}

	/**
	 * <h1>public {@link GNodeMaster} getGuiMaster()</h1>
	 * <p>
	 * Returns the {@link GNodeMaster} of the nodesystem. It is needed for a lot of
	 * things, for more information see {@link GNodeMaster}
	 * </p>
	 * 
	 * @return the gui-nodemaster of the nodesystem.
	 */
	public GNodeMaster getGuiMaster() {
		return nodeMaster;
	}

	/**
	 * <h1>public void update()</h1>
	 * <p>
	 * Has to be called when a node has been:
	 * <ul>
	 * <li>Removed</li>
	 * <li>Added</li>
	 * <li>Changed</li>
	 * <li>Moved</li>
	 * <li>You get the idea....</li>
	 * </ul>
	 * 
	 * Just update whenever you change something.
	 * </p>
	 */
	public void update() {
		getNodeLayer().getChildren().addAll(nodeMaster.getAddedCells());
		getLineLayer().getChildren().addAll(nodeMaster.getAddedEdges());
		getNodeLayer().getChildren().removeAll(nodeMaster.getRemovedCells());
		getLineLayer().getChildren().removeAll(nodeMaster.getRemovedEdges());
		getNodeLayer().toFront();
		// getLineLayer().toFront();
		for (GNode cell : nodeMaster.getAddedCells()) {
			handler.addMouseHandler(cell);
		}

		getGuiMaster().attachOrphansToGraphParent(nodeMaster.getAddedCells());
		getGuiMaster().disconnectFromGraphParent(nodeMaster.getRemovedCells());

		if (getActive() != null) {
			String baseNode = "";
			baseNode = getActive().toString();
			nodeInfo.setText(SystemUsage.getRamInfo() + "\n" + baseNode);

		} else
			nodeInfo.setText(SystemUsage.getRamInfo());

		nodeMaster.getAddedCells().clear();
		nodeMaster.getAddedEdges().clear();
		nodeMaster.getRemovedCells().clear();
		nodeMaster.getRemovedEdges().clear();

	}

	/**
	 * <h1>public {@link Double} getScale()</h1>
	 * <p>
	 * This method returns the scale-value of the {@link ZoomHandler}, which can be
	 * found in the {@link GGraphScene} object.
	 * </p>
	 * 
	 * @return the scale value of the nodes.
	 */
	public double getScale() {
		return this.getScaleValue();
	}

	private void setDefaulSettings() {
		log(Level.INFO, "Setting defaults...");
		addSetting(GraphSettings.SETTING_CURVE_WIDTH, 4d);
		addSetting(GraphSettings.SETTING_CURVE_CURVE, 50d);
		addSetting(GraphSettings.COLOR_SHADOW_COLOR, 0.1d);
		addSetting(GraphSettings.SETTING_SHADOW_WIDTH, 5d);
		addSetting(GraphSettings.SETTING_SHADOW_HEIGHT, 5d);
		addSetting(GraphSettings.SETTING_SHADOW_RADIUS, 20d);

	}

	private void setDefaultColorLookup() {
		log(Level.INFO, "Setting up default color lookup...");
		getColorLookup().put(Base.OUTPUT, Color.LIGHTBLUE);
		getColorLookup().put(Base.PATH, Color.DARKSEAGREEN);

		for (INodeType t : Arduino.values())
			getColorLookup().put(t, new Color(0, (double) 152 / 255d, (double) 157 / 255d, 1));

		for (INodeType t : ArduinoPinMode.values())

			getColorLookup().put(t, new Color(0, (double) 152 / 255d, (double) 157 / 255d, 1));

		for (INodeType t : Noise.values())
			getColorLookup().put(t, Color.LIGHTCORAL);

		for (INodeType t : ImageHelper.values())
			getColorLookup().put(t, Color.DARKGREEN);

		for (INodeType t : Terrain.values())
			getColorLookup().put(t, Color.BLUEVIOLET);

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

	/**
	 * 
	 * @param string
	 * @param c
	 */
	public void addNodeColorLookup(GraphSettings string, Color c) {
		this.nodeLookup.put(string, c);
	}

	public HashMap<GraphSettings, Color> getGeneralColorLookup() {
		return this.nodeLookup;
	}

	/**
	 * <h1>public void addColorLookup({@link INodeType}, {@link Color})</h1>
	 * <hr>
	 * <p>
	 * This method allows the user to add custom colors to nodes.
	 * </p>
	 * 
	 * @param type
	 *            the type you want to define or change the color
	 * @param c
	 *            the color
	 */
	public void addColorLookup(INodeType type, Color c) {
		this.colorLookup.put(type, c);
	}

	public HashMap<INodeType, Color> getColorLookup() {
		return colorLookup;
	}

	/**
	 * <h1>public void clearGraph()</h1>
	 * <p>
	 * Clears everything that is currently drawn in the graph.
	 * </p>
	 */
	public void clearGraph() {
		nodeMaster.removeAll();
		update();
	}

	public void setGraphScene(Scene sc) {
		this.setNodeScene(sc);
	}

	public Scene getNodeScene() {
		return sc;
	}

	public void setNodeScene(Scene sc) {
		this.sc = sc;
	}

	public Map<INodeType, Class<? extends GNode>> getNodeMap() {
		return nodeMap;
	}

}