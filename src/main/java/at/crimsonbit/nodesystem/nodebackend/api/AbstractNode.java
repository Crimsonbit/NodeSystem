package at.crimsonbit.nodesystem.nodebackend.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import at.crimsonbit.nodesystem.nodebackend.util.NodeConnection;

/**
 * 
 * @author alex
 *
 */
public abstract class AbstractNode {

	@NodeType
	private static final INodeType type = NoNodeType.ABSTRACT;

	Map<Field, NodeConnection> connections;
	// int id;
	@InjectNodeMaster
	protected NodeMaster master;

	public AbstractNode() {
		connections = new HashMap<>();
	}

	public final boolean set(String key, Object value) {
		try {
			Field f = master.getField(this.getClass(), key);
			if (f != null) {
				f.set(this, value);
				return true;
			} else
				return false;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot access key " + key + " in Node " + this, e);
		}
	}

	/**
	 * Calculates an output of this node and returns it. The Output is specified by
	 * the Name of the Field. <br>
	 * Internally, this method first checks if the string output actually names a
	 * Field annotated with {@link NodeOutput} in the class. Then it queries the
	 * NodeMaster for all inputs and checks the connections to them. If an input is
	 * connected, then the get Function is called on the connected Node, in order to
	 * get the input value. If there is no Connection at this input, then
	 * {@link AbstractNode#setFieldToDefault(Field)} is called to set the default
	 * value. This method can be overridden to have different default values.
	 * 
	 * @param key
	 * @return The object which was calculated or null if not possible
	 */
	public final Object get(String key) {
		try {
			Field f = master.getOutput(this.getClass(), key);
			if (f != null) {
				// return f.get(this);
				return doGet(f);
			} else
				return null;
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot get output value for " + key + " in Node " + this, e);
		}
	}

	private final Object doGet(Field f) throws IllegalArgumentException, IllegalAccessException {
		Collection<Field> inputs = master.getAllInputs(this);
		for (Field in : inputs) {
			NodeConnection con = connections.get(in);
			if (con == null) {
				setFieldToDefault(f);
			} else {
				in.set(this, con.getNodeInstance().doGet(con.getField()));
			}
		}
		NodeOutput outDefinition = f.getAnnotation(NodeOutput.class);
		try {
			Method m = this.getClass().getDeclaredMethod(outDefinition.value());
			m.setAccessible(true);
			m.invoke(this);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException("Compute method " + outDefinition.value() + " not defined in class "
					+ this.getClass().getCanonicalName(), e);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
		return f.get(this);
	}

	/**
	 * Sets the value of a Field in the Node to Default. Is used for inputs
	 * 
	 * @param f
	 *            The Field
	 * @throws IllegalAccessException
	 *             If there is a Security Manager and it doesn't permit changing the
	 *             field
	 */
	protected void setFieldToDefault(Field f) throws IllegalAccessException {
		Class<?> type = f.getType();
		if (type.isPrimitive()) {
			if (type == int.class)
				f.setInt(this, 0);
			else if (type == float.class)
				f.setFloat(this, 0.0f);
			else if (type == double.class)
				f.setDouble(this, 0.0);
			else if (type == long.class)
				f.setLong(this, 0l);
			else if (type == short.class)
				f.setShort(this, (short) 0);
			else if (type == byte.class)
				f.setByte(this, (byte) 0);
			else if (type == boolean.class)
				f.setBoolean(this, false);
			else if (type == char.class)
				f.setChar(this, '\0');
			else {
				throw new IllegalArgumentException(
						"Type of field is primitive, but none of the known Types, don't know how to handle default value for this");
			}

		} else {
			f.set(this, null);
		}
	}

	public NodeMaster getNodeMaster() {
		return master;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj.getClass() != this.getClass())
			return false;
		AbstractNode other = (AbstractNode) obj;
		if (this.master != other.master)
			return false;
		return this.master.getIdOfNode(this) == this.master.getIdOfNode(other);
	}

}
