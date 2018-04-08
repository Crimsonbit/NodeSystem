package at.crimsonbit.nodesystem.nodebackend.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.util.NodeConnection;

public class NodeMaster {
	private final Map<INodeType, Class<? extends AbstractNode>> registeredNodes;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> inputKeyMap;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> outputKeyMap;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> fieldKeyMap;

	public NodeMaster() {
		inputKeyMap = new HashMap<>();
		registeredNodes = new HashMap<>();
		outputKeyMap = new HashMap<>();
		fieldKeyMap = new HashMap<>();
	}

	/**
	 * Registers all Nodes in the package and all subpackages specified by path. A
	 * Node is any class, that extends AbstractNode
	 * 
	 * @param path
	 */
	public void registerNodes(String path) {
		Reflections ref = new Reflections(path);
		for (Class<? extends AbstractNode> clazz : ref.getSubTypesOf(AbstractNode.class)) {

			inputKeyMap.put(clazz, new HashMap<>());
			outputKeyMap.put(clazz, new HashMap<>());
			fieldKeyMap.put(clazz, new HashMap<>());

			try {

				Field[] decF = clazz.getDeclaredFields();
				for (Field f : decF) {
					if (f.isAnnotationPresent(NodeType.class)) {
						if (!f.isAccessible()) {
							f.setAccessible(true);
						}
						registeredNodes.put((INodeType) f.get(null), clazz);
					}
				}

				populateKeys(clazz, inputKeyMap.get(clazz), NodeInput.class);
				populateKeys(clazz, outputKeyMap.get(clazz), NodeOutput.class);
				populateKeys(clazz, fieldKeyMap.get(clazz), NodeField.class);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("Field annotated with @NodeType in class " + clazz.getCanonicalName()
						+ " is not static, but must be", e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException("Field annotated with @NodeType is not accessible", e);
			}
		}
	}

	private void populateKeys(Class<? extends AbstractNode> clazz, Map<String, Field> map,
			Class<? extends Annotation> annotation) {
		if (clazz == AbstractNode.class) {
			return;
		}

		Field[] decF = clazz.getDeclaredFields();
		for (Field f : decF) {
			if (f.isAnnotationPresent(annotation)) {
				f.setAccessible(true);
				map.put(f.getName(), f);
			}
		}

		Class<?> superclass = clazz.getSuperclass();
		if (AbstractNode.class.isAssignableFrom(superclass)) {
			populateKeys(superclass.asSubclass(AbstractNode.class), map, annotation);
		}

	}

	public Set<INodeType> getAllNodeClasses() {
		return registeredNodes.keySet();
	}

	protected Collection<Field> getAllInputs(Class<? extends AbstractNode> clazz) {
		return inputKeyMap.get(clazz).values();
	}

	protected Collection<Field> getAllInputs(AbstractNode node) {
		return getAllInputs(node.getClass());
	}

	protected Field getOutput(Class<? extends AbstractNode> clazz, String key) {
		return outputKeyMap.get(clazz).get(key);
	}

	protected Field getField(Class<? extends AbstractNode> clazz, String key) {
		return fieldKeyMap.get(clazz).get(key);
	}

	public Set<String> getAllInputNames(Class<? extends AbstractNode> clazz) {
		return inputKeyMap.get(clazz).keySet();
	}

	public Set<String> getAllInputNames(AbstractNode node) {
		return getAllInputNames(node.getClass());
	}

	public Set<String> getAllOutputNames(Class<? extends AbstractNode> clazz) {
		return outputKeyMap.get(clazz).keySet();
	}

	public Set<String> getAllOutputNames(AbstractNode node) {
		return getAllOutputNames(node.getClass());
	}

	public Set<String> getAllFieldNames(Class<? extends AbstractNode> clazz) {
		return fieldKeyMap.get(clazz).keySet();
	}

	public Set<String> getAllFieldNames(AbstractNode node) {
		return getAllFieldNames(node.getClass());
	}

	public AbstractNode createNode(INodeType type) {
		Class<? extends AbstractNode> clazz = registeredNodes.get(type);
		if (clazz == null) {
			throw new IllegalArgumentException("Node with type " + type + " is not registered");
		}
		return createNode(clazz);
	}

	public AbstractNode createNode(Class<? extends AbstractNode> clazz) {

		try {
			AbstractNode node = clazz.newInstance();
			node.master = this;
			return node;
		} catch (InstantiationException e) {
			throw new IllegalStateException(
					"Class " + clazz.getCanonicalName() + " does not have no-arg constructor, but must to", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to access constructor of " + clazz.getCanonicalName(), e);
		}
	}

	public boolean setConnection(AbstractNode inNode, String input, AbstractNode outNode, String out)
			throws NoSuchNodeException {

		Map<String, Field> regIns = inputKeyMap.get(inNode.getClass());
		if (regIns == null) {
			throw new NoSuchNodeException(
					"No Node with class " + inNode.getClass().getCanonicalName() + " is registered");
		}
		Map<String, Field> regOuts = outputKeyMap.get(outNode.getClass());
		if (regOuts == null) {
			throw new NoSuchNodeException(
					"No Node with class " + outNode.getClass().getCanonicalName() + " is registered");
		}

		Field inField = regIns.get(input);
		Field outField = regOuts.get(out);

		if (inField == null) {
			throw new NoSuchNodeException("Node " + inNode.getClass().getName() + " has no input " + input);
		}

		if (outField == null) {
			throw new NoSuchNodeException("Node " + outNode.getClass().getName() + " has no input " + out);
		}

		inNode.connections.put(inField, new NodeConnection(outField, outNode));

		return true;
	}

	/**
	 * Remove a Connection to the input of this node
	 * 
	 * @param inNode
	 *            The Node
	 * @param input
	 *            The name of the input
	 * @return If there was a Connection on this input
	 * @throws NoSuchNodeException
	 *             If the Node is not registered or the registered Node has no input
	 *             Field named input
	 */
	public boolean removeConnection(AbstractNode inNode, String input) throws NoSuchNodeException {
		Map<String, Field> regIns = inputKeyMap.get(inNode.getClass());
		if (regIns == null) {
			throw new NoSuchNodeException(
					"No Node with class " + inNode.getClass().getCanonicalName() + " is registered");
		}
		Field inField = regIns.get(input);
		if (inField == null) {
			throw new NoSuchNodeException("Node " + inNode.getClass().getName() + " has no input " + input);
		}

		try {
			inNode.setFieldToDefault(inField);
		} catch (IllegalAccessException e) {
			throw new NoSuchNodeException("Could not set the disconnected Field " + input + " to default in " + inNode,
					e);
		}

		return inNode.connections.remove(inField) != null;

	}

}
