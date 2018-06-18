package at.crimsonbit.nodesystem.nodebackend.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ObjectInputStreamWithLoader extends ObjectInputStream {

	private ClassLoader loader;
	protected static final HashMap<String, Class<?>> primitives;

	static {
		try {
			Field f = ObjectInputStream.class.getDeclaredField("primClasses");
			f.setAccessible(true);
			primitives = (HashMap<String, Class<?>>) f.get(null);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	public ObjectInputStreamWithLoader(InputStream in, ClassLoader loader) throws IOException {
		super(in);
		assert loader != null;
		this.loader = loader;
	}

	@Override
	protected Class<?> resolveClass(ObjectStreamClass desc) throws IOException, ClassNotFoundException {
		String name = desc.getName();
		try {
			return Class.forName(name, false, loader);
		} catch (ClassNotFoundException ex) {
			Class<?> cl = primitives.get(name);
			if (cl != null) {
				return cl;
			} else {
				throw ex;
			}
		}
	}

}
