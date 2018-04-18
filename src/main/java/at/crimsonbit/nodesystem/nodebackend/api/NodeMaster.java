package at.crimsonbit.nodesystem.nodebackend.api;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.reflections.Reflections;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import at.crimsonbit.nodesystem.nodebackend.api.dto.ConnectionDTO;
import at.crimsonbit.nodesystem.nodebackend.api.dto.FieldDTO;
import at.crimsonbit.nodesystem.nodebackend.api.dto.NodeDTO;
import at.crimsonbit.nodesystem.nodebackend.api.dto.RegistryDTO;
import at.crimsonbit.nodesystem.nodebackend.api.dto.Signal;
import at.crimsonbit.nodesystem.nodebackend.misc.NoSuchNodeException;
import at.crimsonbit.nodesystem.nodebackend.util.NodeConnection;
import at.crimsonbit.nodesystem.nodebackend.util.Tuple;

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
	private static final String ENTRY_EXTRA_NAME = "extra.dat";
	private static final String ENTRY_CONN_NAME = "connections.dat";
	private static final String ENTRY_STATE_NAME = "state.dat";
	private static final String ENTRY_REG_NAME = "registry.dat";
	private final BiMap<INodeType, Class<? extends AbstractNode>> registeredNodes;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> inputKeyMap;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> outputKeyMap;
	private final Map<Class<? extends AbstractNode>, Map<String, Field>> fieldKeyMap;
	// map to enable creating Nodes by using strings
	private final Map<String, INodeType> stringToType;
	private final BiMap<Integer, AbstractNode> nodePool;

	private Function<AbstractNode, Object> getExtraInfo;

	private int id;

	private transient Map<AbstractNode, Object> extraInfo;

	public NodeMaster() {
		inputKeyMap = new HashMap<>();
		registeredNodes = HashBiMap.<INodeType, Class<? extends AbstractNode>>create();
		outputKeyMap = new HashMap<>();
		fieldKeyMap = new HashMap<>();
		stringToType = new HashMap<>();
		nodePool = HashBiMap.<Integer, AbstractNode>create();

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
	 * Returns a Set of all input names of a NodeClass
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllInputNames(int id) {
		return inputKeyMap.get(getNodeByID(id).getClass()).keySet();
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
	 * Returns a Set of all ouput names of a Node
	 * 
	 * @param clazz
	 * @return
	 */
	public Set<String> getAllOutputNames(int node) {
		return getAllOutputNames(getNodeByID(node));
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
	 * Returns the type of the AbstractNode node
	 * 
	 * @param node
	 * @return
	 */
	public INodeType getTypeOfNode(AbstractNode node) {
		return registeredNodes.inverse().get(node.getClass());
	}

	/**
	 * Returns the type of the AbstractNode node
	 * 
	 * @param node
	 * @return
	 */
	public INodeType getTypeOfNode(int node) {
		return getTypeOfNode(getNodeByID(node));
	}

	/**
	 * 
	 * @return
	 */
	public Collection<AbstractNode> getAllNodes() {
		return nodePool.values();
	}

	public AbstractNode getNodeByID(int id) {
		return nodePool.get(id);
	}

	public int getIdOfNode(AbstractNode node) {
		return nodePool.inverse().get(node);
	}

	/**
	 * Returns the connections of a Node as Immutable Map
	 * 
	 * @param node
	 * @return
	 */
	public ImmutableMap<Field, NodeConnection> getConnections(AbstractNode node) {
		return ImmutableMap.<Field, NodeConnection>builder().putAll(node.connections).build();
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
		return doCreateNode(clazz);
	}

	/**
	 * Deletes the Node with the given id.
	 * 
	 * @param id
	 * @return true if the node existed and was deleted, false if otherwise
	 */
	public boolean deleteNode(int id) {
		return nodePool.remove(id) != null;
	}

	/**
	 * Searches for the Node node and deletes it if present.
	 * 
	 * @param node
	 * @return true if the node existed and was deleted, false if otherwise
	 */
	public boolean deleteNode(AbstractNode node) {
		return nodePool.inverse().remove(node) != null;
	}

	public AbstractNode copyOfNode(AbstractNode other) {
		AbstractNode node = createNode(other.getClass());
		for (Field f : getAllFields(other)) {
			try {
				f.set(node, f.get(other));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return node;

	}

	public int copyOfNode(int other) {
		return getIdOfNode(copyOfNode(getNodeByID(other)));
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
	public int createNodeId(Class<? extends AbstractNode> clazz) {
		return getIdOfNode(createNode(clazz));
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
	public int createNodeId(INodeType type) {
		return getIdOfNode(createNode(type));
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
			putNewNode(node, id++);
			return node;
		} catch (InstantiationException e) {
			throw new IllegalStateException(
					"Class " + clazz.getCanonicalName() + " does not have no-arg constructor, but must to", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to access constructor of " + clazz.getCanonicalName(), e);
		}

	}

	private AbstractNode doCreateNode(Class<? extends AbstractNode> clazz, int id) {
		try {
			AbstractNode node = clazz.newInstance();
			node.master = this;
			putNewNode(node, id);
			return node;
		} catch (InstantiationException e) {
			throw new IllegalStateException(
					"Class " + clazz.getCanonicalName() + " does not have no-arg constructor, but must to", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("Failed to access constructor of " + clazz.getCanonicalName(), e);
		}

	}

	private void putNewNode(AbstractNode node, int id) {
		nodePool.put(id, node);
	}

	/**
	 * Connect an output of a Node with an input of another Node. This connection
	 * can later be removed by using
	 * {@link NodeMaster#removeConnection(AbstractNode, String)} The Connection is
	 * stored in the Node which contains the input, so that it can load its input
	 * values when an output is requested see {@link AbstractNode#get(String)} for
	 * more info on how the getting works
	 * 
	 * @param inNode
	 *            The Node which contains the input
	 * @param input
	 *            the name of the input
	 * @param outNode
	 *            The Node which contains the output
	 * @param out
	 *            the name of the output
	 * @return true if the connection was created, false if not
	 * @throws NoSuchNodeException
	 *             If one of the Node Classes is not registered or the in/output
	 *             field could not be found in the class
	 */
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

	private boolean trySaveExtraInfo(ZipOutputStream zos) throws IOException {
		ZipEntry e = new ZipEntry(ENTRY_EXTRA_NAME);
		e.setMethod(ZipEntry.DEFLATED);
		zos.putNextEntry(e);
		ObjectOutputStream oos = new ObjectOutputStream(zos);
		for (AbstractNode node : nodePool.values()) {
			oos.writeObject(new Tuple<Integer, Object>(getIdOfNode(node), getExtraInfo.apply(node)));
		}
		oos.writeObject(null);
		oos.flush();
		zos.closeEntry();
		return true;
	}

	private boolean trySaveRegistry(ZipOutputStream zos) throws IOException {
		ZipEntry e = new ZipEntry(ENTRY_REG_NAME);
		e.setMethod(ZipEntry.DEFLATED);
		zos.putNextEntry(e);
		ObjectOutputStream oos = new ObjectOutputStream(zos);
		Iterator<Map.Entry<INodeType, Class<? extends AbstractNode>>> iter = registeredNodes.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<INodeType, Class<? extends AbstractNode>> entry = iter.next();
			oos.writeObject(new RegistryDTO(entry.getKey(), entry.getValue()));
		}
		oos.writeObject(Signal.EOF);
		oos.flush();
		zos.closeEntry();
		return true;
	}

	private boolean trySaveState(ZipOutputStream zos) throws IOException, IllegalAccessException {
		ZipEntry e = new ZipEntry(ENTRY_STATE_NAME);
		e.setMethod(ZipEntry.DEFLATED);
		zos.putNextEntry(e);
		ObjectOutputStream oos = new ObjectOutputStream(zos);
		// Convert to array, because indexes are needed afterwards
		AbstractNode[] allNodes = new AbstractNode[nodePool.size()];
		int j = 0;
		for (Map.Entry<Integer, AbstractNode> node : nodePool.entrySet()) {
			allNodes[j++] = node.getValue();
		}
		Comparator<? super AbstractNode> comp = (a, b) -> Integer.compareUnsigned(a.hashCode(), b.hashCode());
		Arrays.sort(allNodes, comp);
		for (int k = 0; k < allNodes.length; k++) {
			AbstractNode node = allNodes[k];
			Map<String, Field> fields = fieldKeyMap.get(node.getClass());
			assert fields != null;
			FieldDTO[] dtos = new FieldDTO[fields.size()];
			int i = 0;
			for (Map.Entry<String, Field> entry : fields.entrySet()) {
				FieldDTO dto = new FieldDTO(entry.getKey(), entry.getValue().get(node));
				dtos[i++] = dto;
			}
			NodeDTO nodeDTO = new NodeDTO(node.getClass(), nodePool.inverse().get(node), dtos);
			oos.writeObject(nodeDTO);
		}

		oos.writeObject(Signal.EOF);
		e = new ZipEntry(ENTRY_CONN_NAME);
		e.setMethod(ZipEntry.DEFLATED);
		zos.closeEntry();
		zos.putNextEntry(e);
		oos.flush();
		oos = new ObjectOutputStream(zos);
		for (int id = 0; id < allNodes.length; id++) {
			AbstractNode node = allNodes[id];
			for (Map.Entry<Field, NodeConnection> entry : node.connections.entrySet()) {
				int in, out;
				in = nodePool.inverse().get(node);
				out = Arrays.binarySearch(allNodes, entry.getValue().getNodeInstance(), comp);
				out = nodePool.inverse().get(allNodes[out]);
				String sIn = entry.getKey().getName();
				String sOut = entry.getValue().getField().getName();
				oos.writeObject(new ConnectionDTO(out, in, sOut, sIn));
			}
		}
		oos.writeObject(Signal.EOF);
		oos.flush();
		zos.closeEntry();
		return true;
	}

	/**
	 * Save the current NodeMaster Instance to the File specified by savefile. If
	 * this file already exists and override is false, this methods returns false
	 * and does nothing, else all important Information is saved to the specified
	 * savefile. It can later be loaded again using {@link NodeMaster#load(String)}
	 * 
	 * @param savefile
	 * @param override
	 * @return
	 */
	public boolean save(String savefile, boolean override) throws IOException {
		Path p = Paths.get(savefile);
		if (Files.exists(p) && !override) {
			return false;
		}
		try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(p))) {
			if (!trySaveRegistry(zos)) {
				return false;
			}
			if (!trySaveState(zos)) {
				return false;
			}
			if (!trySaveExtraInfo(zos)) {
				return false;
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Loads a saved NodeMaster from a savefile created by
	 * {@link NodeMaster#save(String, boolean)}
	 * 
	 * @param savefile
	 * @return
	 * @throws IOException
	 * @throws NoSuchNodeException
	 */
	public static NodeMaster load(String savefile) throws IOException, NoSuchNodeException {
		NodeMaster m = new NodeMaster();
		if (!m.tryLoad(savefile))
			return null;
		return m;
	}

	@SuppressWarnings("unchecked")
	public <T> void setExtraInfoSavingFunction(Function<AbstractNode, T> f) {
		this.getExtraInfo = (Function<AbstractNode, Object>) f;
	}

	@SuppressWarnings("unchecked")
	public <T> void getExtraInfo(BiConsumer<AbstractNode, T> f) {
		if (extraInfo != null) {
			for (Map.Entry<AbstractNode, Object> entry : extraInfo.entrySet()) {
				f.accept(entry.getKey(), (T) entry.getValue());
			}
		}
		extraInfo = null;
	}

	private boolean tryLoad(String savefile) throws IOException, NoSuchNodeException {
		Path p = Paths.get(savefile);
		if (!Files.exists(p)) {
			return false;
		}
		try (ZipInputStream zin = new ZipInputStream(Files.newInputStream(p))) {
			ZipEntry e = null;
			while ((e = zin.getNextEntry()) != null) {
				switch (e.getName()) {
				case ENTRY_REG_NAME:
					if (!tryLoadRegistry(zin)) {
						return false;
					}
					break;
				case ENTRY_STATE_NAME:
					if (!tryLoadState(zin)) {
						return false;
					}
					break;
				case ENTRY_CONN_NAME:
					if (!tryLoadConnections(zin)) {
						return false;
					}
					break;
				case ENTRY_EXTRA_NAME:
					if (!tryLoadExtraInfo(zin)) {
						return false;
					}
					break;
				}

			}
		}
		return true;
	}

	private boolean tryLoadRegistry(ZipInputStream zin) throws IOException {
		ObjectInputStream ois = new ObjectInputStream(zin);
		Object read;
		try {
			while ((read = ois.readObject()) != null) {
				if (read.getClass() != RegistryDTO.class) {
					if (Signal.EOF.equals(read)) {
						break;
					}
					throw new IllegalStateException("Expected RegistryDTO, but got " + read.getClass().getName()
							+ " Maybe the savefile is corrupt");
				}
				RegistryDTO dto = (RegistryDTO) read;
				registeredNodes.put(dto.type, dto.clazz);
				stringToType.put(dto.type.toString(), dto.type);
				inputKeyMap.put(dto.clazz, new HashMap<>());
				outputKeyMap.put(dto.clazz, new HashMap<>());
				fieldKeyMap.put(dto.clazz, new HashMap<>());
				populateKeys(dto.clazz, inputKeyMap.get(dto.clazz), NodeInput.class);
				populateKeys(dto.clazz, outputKeyMap.get(dto.clazz), NodeOutput.class);
				populateKeys(dto.clazz, fieldKeyMap.get(dto.clazz), NodeField.class);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean tryLoadExtraInfo(ZipInputStream zin) throws IOException {

		ObjectInputStream ois = new ObjectInputStream(zin);
		Object read;
		try {
			while ((read = ois.readObject()) != null) {
				if (extraInfo == null)
					extraInfo = new HashMap<>();
				if (read.getClass() != Tuple.class) {
					throw new IllegalStateException(
							"Expected Tuple but got " + read.getClass().getName() + " Maybe the savefile is corrupt");
				}
				@SuppressWarnings("unchecked")
				Tuple<Integer, Object> tuple = (Tuple<Integer, Object>) read;
				extraInfo.put(getNodeByID(tuple.a), tuple.b);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private boolean tryLoadState(ZipInputStream zin) throws IOException, NoSuchNodeException {

		ObjectInputStream ois = new ObjectInputStream(zin);
		Object read;
		NodeDTO[] allNodes;
		try {
			Set<NodeDTO> nodes = new HashSet<>();
			while ((read = ois.readObject()) != null) {
				if (read.getClass() != NodeDTO.class) {
					if (Signal.EOF.equals(read)) {
						break;
					}
					throw new IllegalStateException("Expected NodeDTO, but got " + read.getClass().getName()
							+ " Maybe the savefile is corrupt");
				}
				NodeDTO dto = (NodeDTO) read;
				nodes.add(dto);
			}
			allNodes = nodes.toArray(new NodeDTO[nodes.size()]);
			Arrays.sort(allNodes, (a, b) -> Integer.compare(a.id, b.id));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		AbstractNode[] realNodes = new AbstractNode[allNodes.length];
		for (int i = 0; i < allNodes.length; i++) {
			Class<? extends AbstractNode> clazz = allNodes[i].clazz;
			AbstractNode node = doCreateNode(clazz, allNodes[i].id);
			for (FieldDTO f : allNodes[i].fields) {
				node.set(f.name, f.value);
			}
			realNodes[i] = node;

		}

		return true;
	}

	private boolean tryLoadConnections(ZipInputStream zin) throws NoSuchNodeException, IOException {
		Object read = null;
		ObjectInputStream ois = new ObjectInputStream(zin);
		try {

			while ((read = ois.readObject()) != null) {
				if (read.getClass() != ConnectionDTO.class) {
					if (Signal.EOF.equals(read)) {
						break;
					}
					throw new IllegalStateException("Expected ConnectionDTO, but got " + read.getClass().getName()
							+ " Maybe the savefile is corrupt");
				}
				ConnectionDTO dto = (ConnectionDTO) read;
				AbstractNode nodeOut = nodePool.get(dto.idOut);
				AbstractNode nodeIn = nodePool.get(dto.idIn);
				setConnection(nodeIn, dto.fieldIn, nodeOut, dto.fieldOut);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public void clear() {
		registeredNodes.clear();
		fieldKeyMap.clear();
		inputKeyMap.clear();
		outputKeyMap.clear();
		if (extraInfo != null)
			extraInfo.clear();
		nodePool.clear();
		stringToType.clear();
	}

}
