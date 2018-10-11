package at.crimsonbit.nodesystem.gui;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

import at.crimsonbit.nodesystem.gui.node.GNode;
import at.crimsonbit.nodesystem.node.IGuiNodeType;
import at.crimsonbit.nodesystem.nodebackend.api.INodeType;

/**
 * <h1>GClipboard</h1>
 * <p>
 * This class is a simple clipboard.
 * </p>
 * 
 * @author Florian Wagner
 *
 */
public class GClipboard
{

	private GNode toCopy;
	private GNodeGraph graph;
	private Object objToCopy;
	private Set<GNode> toCopySet;
	private static GClipboard instance;

	public static GClipboard getClipboard(GNodeGraph graph)
	{
		if (instance == null)
		{
			instance = new GClipboard(graph);
		}
		return instance;
	}

	private GClipboard(GNodeGraph graph)
	{
		this.graph = graph;
	}

	public void copy(Object o)
	{
		objToCopy = 0;
	}

	public void copy(GNode node)
	{
		this.toCopy = node;
	}

	public void copy(Set<GNode> set)
	{
		toCopySet = set;
	}

	public void pasteNodeGroup()
	{
		if (this.graph != null && this.toCopySet != null)
		{
			for (GNode nnew : toCopySet)
			{
				IGuiNodeType type = (IGuiNodeType) nnew.getNodeType();
				Class<? extends GNode> clazz = ((IGuiNodeType) (type)).getCustomNodeClass();
				Constructor<? extends GNode> con;
				try
				{
					con = clazz.getConstructor(String.class, INodeType.class, boolean.class, GNodeGraph.class,
							double.class, double.class);
					GNode n = con.newInstance(nnew.getTypeName(), type, true, graph,
							graph.getCurX() + nnew.getLayoutX(), graph.getCurY() + nnew.getLayoutY());
					graph.getGuiMaster().addNode(n);
					n.toFront();
				} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
						| IllegalArgumentException | InvocationTargetException e)
				{
					e.printStackTrace();
				}
				graph.update();
			}

		}

	}

	public void copyPaste(GNode node)
	{
		if (this.graph != null && node != null)
		{

			IGuiNodeType type = (IGuiNodeType) node.getNodeType();

			Class<? extends GNode> clazz = type.getCustomNodeClass();
			Constructor<? extends GNode> con;
			try
			{
				con = clazz.getConstructor(String.class, INodeType.class, boolean.class, GNodeGraph.class, double.class,
						double.class);
				GNode n = con.newInstance(node.getTypeName(), type, true, graph, graph.getCurX(), graph.getCurY());
				graph.getGuiMaster().addNode(n);
				n.toFront();
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
			graph.update();
		}
	}

	public void paste()
	{
		if (this.graph != null && this.toCopy != null)
		{
			/*
			 * System.out.println(this.toCopy.getTypeName());
			 * System.out.println(this.toCopy.getName());
			 * System.out.println(this.toCopy.getNodeType()); INodeType type =
			 * graph.getGuiMaster().getNodeMaster().getTypeByName(this.toCopy.getTypeName())
			 * ;
			 * 
			 * System.out.println(type); if (type != null) { Class<? extends GNode> clazz =
			 * ((IGuiNodeType) type).getCustomNodeClass(); Constructor<? extends GNode> con;
			 * try { con = clazz.getConstructor(String.class, INodeType.class,
			 * boolean.class, GNodeGraph.class, double.class, double.class); GNode n =
			 * con.newInstance(this.toCopy.getTypeName(), type, true, graph,
			 * graph.getCurX(), graph.getCurY()); graph.getGuiMaster().addNode(n); } catch
			 * (NoSuchMethodException | SecurityException | InstantiationException |
			 * IllegalAccessException | IllegalArgumentException | InvocationTargetException
			 * e) { e.printStackTrace(); }
			 * 
			 * graph.update(); graph.removeBlur(); }
			 */
			// INodeType type =
			// graph.getGuiMaster().getNodeMaster().getTypeByName(this.toCopy.getTypeName());
			IGuiNodeType type = (IGuiNodeType) this.toCopy.getNodeType();

			Class<? extends GNode> clazz = ((IGuiNodeType) type).getCustomNodeClass();
			Constructor<? extends GNode> con;
			try
			{
				con = clazz.getConstructor(String.class, INodeType.class, boolean.class, GNodeGraph.class, double.class,
						double.class);
				GNode n = con.newInstance(this.toCopy.getTypeName(), type, true, graph, graph.getCurX(),
						graph.getCurY());
				// Class<? extends GNode> clazz = type.getCustomNodeClass();
				// Constructor<? extends GNode> con;
				// try {
				// con = clazz.getConstructor(String.class, INodeType.class, boolean.class,
				// GNodeGraph.class, double.class,
				// double.class);
				// GNode n = con.newInstance(this.toCopy.getTypeName(), type, true, graph,
				// graph.getCurX(),
				// graph.getCurY());
				graph.getGuiMaster().addNode(n);
				n.toFront();
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e)
			{
				e.printStackTrace();
			}
			graph.update();

		}
	}
}