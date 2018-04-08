package at.crimsonbit.nodebackend.api;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import at.crimsonbit.nodebackend.util.NodeConnection;

/**
 * 
 * @author alex
 *
 */
public abstract class AbstractNode {

	@NodeType
	private static final INodeType type = NoNodeType.ABSTRACT;

	Map<Field, NodeConnection> connections;

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
				in.set(this, null);
			} else {
				in.set(this, con.getNodeInstance().doGet(con.getField()));
			}
		}
		NodeOutput outDefinition = f.getAnnotation(NodeOutput.class);
		try {
			Method m = this.getClass().getDeclaredMethod(outDefinition.value());
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
}
