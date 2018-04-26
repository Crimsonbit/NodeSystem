package at.crimsonbit.nodesystem.gui;

import java.util.logging.Level;

import at.crimsonbit.nodesystem.util.logger.SystemLogger;
import javafx.scene.Scene;

/**
 * <h1>NodeSystemBuilder</h1>
 * <p>
 * The NodeSystemBuilder class allows the user to create/build the GNodeGraph
 * object.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class NodeSystemBuilder {

	private GNodeSystem sys;
	private GNodeView view;
	private GNodeGraph graph;
	private Scene scene;
	private double w;
	private double h;

	/**
	 * Constructor.
	 * 
	 * @param width
	 *            width of the nodegraph scene
	 * @param height
	 *            height of the nodegraph scene
	 */
	public NodeSystemBuilder(double width, double height) {
		this.w = width;
		this.h = height;

	}

	/**
	 * <h1>init()</h1>
	 * <p>
	 * Initializes the NodeSystemBuilder. This has to be called before all other
	 * methods with the exception of the {@link NodeSystemBuilder#attachLogger()}
	 * function. If you want to attach the logger to the nodegraph, you have to call
	 * it before the {@link NodeSystemBuilder#init()} method!
	 * </p>
	 * 
	 * @return this
	 */
	public NodeSystemBuilder init() {
		sys = new GNodeSystem();
		view = sys.getNodeView();
		graph = view.getNodeGraph();
		scene = new Scene(view, w, h);
		graph.setGraphScene(scene);
		return this;
	}

	/**
	 * <h1>attachSettingsPane()</h1>
	 * <p>
	 * Attaches a settings pane to the nodegraph. Currently a work in progress ->
	 * DOES NOTHING!
	 * </p>
	 * 
	 * @return this
	 */
	public NodeSystemBuilder attachSettingsPane() {
		sys.attachSettingsPane(true);
		view = sys.getNodeView();
		graph = view.getNodeGraph();
		scene = new Scene(view, w, h);
		graph.setGraphScene(scene);
		return this;
	}

	/**
	 * <h1>registerCustomNodes({@link String}</h1>
	 * <p>
	 * This method can be called as many times as neccessary! With this method you
	 * can register your own custom nodes by telling this method the package as
	 * string of your nodes.
	 * </p>
	 * 
	 * @param pack
	 *            the package name as string
	 * @return this
	 */
	public NodeSystemBuilder registerCustomNodes(String pack) {
		if (graph != null)
			graph.registerNodes(pack);

		return this;
	}

	/**
	 * <h1>attachLogger()</h1>
	 * <p>
	 * Attaches a logger to the nodegraph. <br>
	 * Attention: You have to call this method <b>BEFORE</b>
	 * {@link NodeSystemBuilder#init()}!
	 * </p>
	 * 
	 * @return this
	 */
	public NodeSystemBuilder attachLogger() {

		SystemLogger.attachLogger();

		return this;
	}

	/**
	 * <h1>registerDefaultNodes({@link Boolean}</h1>
	 * <p>
	 * Registers the default nodes of the nodegraph.
	 * </p>
	 * 
	 * @param f
	 *            if you want to register the default nodes, pass true, otherwise
	 *            false
	 * @return this
	 */
	public NodeSystemBuilder registerDefaultNodes(boolean f) {
		if (graph != null)
			graph.initGraph(f);

		return this;
	}

	/**
	 * <h1>attachInfo()</h1>
	 * <p>
	 * Attaches additional information about the nodes, and the nodegraph, to the
	 * graph as text.
	 * </p>
	 * 
	 * @return this
	 */
	public NodeSystemBuilder attachInfo() {
		if (graph != null)
			graph.addInfo();
		return this;
	}

	/**
	 * <h1>build()</h1>
	 * <p>
	 * Builds the nodegraph. It returns the {@link GNodeGraph} which was created
	 * using the build/ attach methods of this class.
	 * 
	 * @return the newly built GNodeGraph object
	 */
	public GNodeGraph build() {
		this.graph.log(Level.INFO, "NodeSystem ready!");
		return this.graph;
	}

}
