package at.crimsonbit.nodesystem.nodebackend.api;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.util.NodeConnection;

/**
 * A Node Master is used to Manage registration and connection of Node, which
 * are represented by Classes, that extend {@link AbstractNode} <br>
 * All Node creation and connection management has to happen through an instance
 * of this class
 * 
 * 
 * @author Alexander Daum
 *
 */
public class NodeMaster {
	private final Map<INodeType, Class<? extends AbstractNode>> registeredNodes;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> inputKeyMap;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> outputKeyMap;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> fieldKeyMap;
	// map to enable creating Nodes by using strings
	private final Map<String, INodeType> stringToType;

	public NodeMaster() {
		inputKeyMap = new HashMap<>();
		registeredNodes = new HashMap<>();
		outputKeyMap = new HashMap<>();
		fieldKeyMap = new HashMap<>();
		stringToType = new HashMap<>();
	}

	/**
	 * Registers all Nodes in the package and all subpackages specified by path. A
	 * Node is any class, that extends AbstractNode. <br>
	 * To be valid it must also have a static final Field annotated with
	 * {@link NodeType}. The Type of this Field has to implement {@link INodeType}
	 * and can be any object, either an instance of a class or an enum.
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
				boolean found = false;
				Field[] decF = clazz.getDeclaredFields();
				for (Field f : decF) {
					if (f.isAnnotationPresent(NodeType.class)) {
						f.setAccessible(true);
						if ((f.getModifiers() & Modifier.STATIC) == 0) {
							throw new IllegalArgumentException("Field annotated with @NodeType in class "
									+ clazz.getCanonicalName() + " is not static, but must be");
						}
						INodeType type = (INodeType) f.get(null);
						if (registeredNodes.containsKey(type)) {
							throw new IllegalArgumentException("The Node "
									+ registeredNodes.get(type).getCanonicalName() + " is registered with type " + type
									+ ", but tried to register " + clazz.getCanonicalName() + " with the same type");
						}
						registeredNodes.put(type, clazz);
						stringToType.put(type.toString(), type);
						found = true;
					}
				}
				if (!found) {
					throw new IllegalArgumentException("Class " + clazz.getCanonicalName()
							+ " extends AbstractNode but does not declare Field annotated with @NodeType");
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

	/**
	 * 
	 * @return a Set of all known Node Types
	 */
	public Set<INodeType> getAllNodeClasses() {
		return registeredNodes.keySet();
	}

	/**
	 * Returns a Collection of all Input Fields in a NodeClass, intended for use in
	 * AbstractNode (No checks for validity are perfomed)
	 * 
	 * @param clazz
	 * @return
	 */
	protected Collection<Field> getAllInputs(Class<? extends AbstractNode> clazz) {
		return inputKeyMap.get(clazz).values();
	}

	/**
	 * Returns a Collection of all Input Fields in a Node, intended for use in
	 * AbstractNode (No checks for validity are perfomed)
	 * 
	 * @param clazz
	 * @return
	 */
	protected Collection<Field> getAllInputs(AbstractNode node) {
		return getAllInputs(node.getClass());
	}

	/**
	 * Returnes the Output with the name key in the NodeClass clazz, or null if it
	 * has none. No check are performed if the NodeClass really is registered
	 * 
	 * @param clazz
	 * @param key
	 * @return
	 */
	protected Field getOutput(Class<? extends AbstractNode> clazz, String key) {
		return outputKeyMap.get(clazz).get(key);
	}

	/**
	 * Returnes the Field with the name key in the NodeClass clazz, or null if it
	 * has none. No check are performed if the NodeClass really is registered
	 * 
	 * @param clazz
	 * @param key
	 * @return
	 */
	protected Field getField(Class<? extends AbstractNode> clazz, String key) {
		return fieldKeyMap.get(clazz).get(key);
	}

	/**
	 * Returns a Set of all input names of a NodeClass
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllInputNames(Class<? extends AbstractNode> clazz) {
		return inputKeyMap.get(clazz).keySet();
	}

	/**
	 * Returns a Set of all input names of a Node
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllInputNames(AbstractNode node) {
		return getAllInputNames(node.getClass());
	}

	/**
	 * Returns a Set of all ouput names of a NodeClass
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllOutputNames(Class<? extends AbstractNode> clazz) {
		return outputKeyMap.get(clazz).keySet();
	}

	/**
	 * Returns a Set of all ouput names of a Node
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllOutputNames(AbstractNode node) {
		return getAllOutputNames(node.getClass());
	}

	/**
	 * Returns a Set of all field names of a NodeClass
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllFieldNames(Class<? extends AbstractNode> clazz) {
		return fieldKeyMap.get(clazz).keySet();
	}

	/**
	 * Returns a Set of all field names of a Node
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllFieldNames(AbstractNode node) {
		return getAllFieldNames(node.getClass());
	}

	/**
	 * Returns all Fields of a NodeClass
	 * 
	 * @param clazz
	 * @return
	 */
	protected Collection<Field> getAllFields(Class<? extends AbstractNode> clazz) {
		return fieldKeyMap.get(clazz).values();
	}

	/**
	 * Returns all Fields of a Node
	 * 
	 * @param clazz
	 * @return
	 */
	protected Collection<Field> getAllFields(AbstractNode node) {
		return getAllFields(node.getClass());
	}

	/**
	 * Gets a INodeType by a string name. The name is stored when the Abstract Node
	 * with this Type is registered, the toString method of the INodeType is used
	 * for that. If there are multiple INodeTypes registered, that have the same
	 * name, then the latest is returned
	 * 
	 * @param name
	 * @return The Node which is registered with this Name, or null if none
	 */
	public INodeType getTypeByName(String name) {
		return stringToType.get(name);
	}

	/**
	 * Returns the type of the field with name field in the Node class of node
	 * 
	 * @param node
	 *            An instance of any NodeClass
	 * @param field
	 *            The name of the field within the Node class, has to be annotated
	 *            with {@link NodeField}
	 * @return
	 * @throws NoSuchNodeException
	 *             If the Class of node is no known NodeType or the Class of node
	 *             has no field with name field
	 */
	public Class<?> getFieldType(AbstractNode node, String field) throws NoSuchNodeException {
		Map<String, Field> fieldMap = fieldKeyMap.get(node.getClass());
		if (fieldMap == null) {
			throw new NoSuchNodeException(
					"No node with class " + node.getClass().getCanonicalName() + " is registered");
		}
		Field f = fieldMap.get(field);
		if (f == null) {
			throw new NoSuchNodeException(
					"No NodeField with name " + field + " in Node Class " + node.getClass().getCanonicalName());
		}
		return f.getType();
	}

	/**
	 * Returns the type of the input with name field in the Node class of node
	 * 
	 * @param node
	 *            An instance of any NodeClass
	 * @param field
	 *            The name of the field within the Node class, has to be annotated
	 *            with {@link NodeInput}
	 * @return
	 * @throws NoSuchNodeException
	 *             If the Class of node is no known NodeType or the Class of node
	 *             has no input with name field
	 */
	public Class<?> getInputType(AbstractNode node, String field) throws NoSuchNodeException {
		Map<String, Field> fieldMap = inputKeyMap.get(node.getClass());
		if (fieldMap == null) {
			throw new NoSuchNodeException(
					"No node with class " + node.getClass().getCanonicalName() + " is registered");
		}
		Field f = fieldMap.get(field);
		if (f == null) {
			throw new NoSuchNodeException(
					"No NodeField with name " + field + " in Node Class " + node.getClass().getCanonicalName());
		}
		return f.getType();
	}

	/**
	 * Returns the type of the output with name field in the Node class of node
	 * 
	 * @param node
	 *            An instance of any NodeClass
	 * @param field
	 *            The name of the field within the Node class, has to be annotated
	 *            with {@link NodeOutput}
	 * @return
	 * @throws NoSuchNodeException
	 *             If the Class of node is no known NodeType or the Class of node
	 *             has no output with name field
	 */
	public Class<?> getOutputType(AbstractNode node, String field) throws NoSuchNodeException {
		Map<String, Field> fieldMap = outputKeyMap.get(node.getClass());
		if (fieldMap == null) {
			throw new NoSuchNodeException(
					"No node with class " + node.getClass().getCanonicalName() + " is registered");
		}
		Field f = fieldMap.get(field);
		if (f == null) {
			throw new NoSuchNodeException(
					"No NodeField with name " + field + " in Node Class " + node.getClass().getCanonicalName());
		}
		return f.getType();
	}

	/**
	 * Creates a new Node with the specified type type. If there is no such
	 * NodeClass registered, an exception is thrown
	 * 
	 * @param type
	 * @return
	 * @throws IllegalArgumentException
	 *             if there is no Node with this type registered
	 */
	public AbstractNode createNode(INodeType type) {
		Class<? extends AbstractNode> clazz = registeredNodes.get(type);
		if (clazz == null) {
			throw new IllegalArgumentException("Node with type " + type + " is not registered");
		}
		for (int i = 0; i < 1000; i++) {
			if (i > 2000) {
				i = 0;
			}
		}
		return doCreateNode(clazz);

	}

	/**
	 * Creates a new Node with the specified type type. If there is no such
	 * NodeClass registered, an exception is thrown
	 * 
	 * @param type
	 * @return
	 * @throws IllegalArgumentException
	 *             if there is no Node with this type registered
	 */
	public AbstractNode createNode(Class<? extends AbstractNode> clazz) {
		if (!registeredNodes.containsValue(clazz))
			throw new IllegalArgumentException("Node with Class " + clazz.getCanonicalName() + " is not registered");
		return doCreateNode(clazz);
	}

	private AbstractNode doCreateNode(Class<? extends AbstractNode> clazz) {
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
