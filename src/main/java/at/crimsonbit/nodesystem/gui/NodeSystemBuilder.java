package at.crimsonbit.nodesystem.gui;

import java.util.logging.Level;

import at.crimsonbit.nodesystem.nodebackend.api.INodeType;
import at.crimsonbit.nodesystem.util.logger.SystemLogger;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

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
	private boolean default_nodes;

	/**
	 * Constructor.
	 * 
	 * @param width
	 *            width of the nodegraph scene
	 * @param height
	 *            height of the nodegraph scene
	 * 
	 * @param log
	 *            tells the nodesystem whether if it should write into a log or not
	 */
	public NodeSystemBuilder(double width, double height, boolean log) {
		this.w = width;
		this.h = height;
		if (log)
			SystemLogger.attachLogger(true, true);
		sys = new GNodeSystem();
		view = sys.getNodeView();
		graph = view.getNodeGraph();
		scene = new Scene(view, w, h);
		graph.setGraphScene(scene);
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
	 * @param c
	 *            the color of the nodes
	 * @return this
	 */
	public NodeSystemBuilder registerCustomNodes(String pckg) {
		if (graph != null)
			graph.registerNodes(pckg);

		return this;
	}

	public NodeSystemBuilder registerColors(Color c, INodeType... types) {
		if (graph != null)
			for (INodeType t : types) {
				graph.addColorLookup(t, c);
			}
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
	 * @param uH
	 *            if you want to enable HTML logging
	 * @param uT
	 *            if you want to enable TXT logging
	 * @return this
	 */
	public NodeSystemBuilder attachLogger(boolean uH, boolean uT) {

		SystemLogger.attachLogger(uH, uT);

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
		default_nodes = f;

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
		if (this.graph != null) {
			graph.initGraph(default_nodes);
			this.graph.log(Level.INFO, "NodeSystem ready!");
			return this.graph;
		}
		return null;
	}

}
