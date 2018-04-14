package at.crimsonbit.nodesystem.util;

import java.util.HashMap;
import java.util.Map;

public class NameSpace {

	private static Map<String, String> nameMap = new HashMap<String, String>();
	private static NameSpace instance;

	public NameSpace() {
		populateNames();
	}

	private void populateNames() {
		addNameSpace("BASE_CONSTANT", "Constant Node");
		addNameSpace("BASE_PATH", "Path Node");
		addNameSpace("BASE_OUTPUT", "Output Node");
	}

	public static NameSpace getInstane() {
		if (instance == null) {
			instance = new NameSpace();
		}
		return instance;
	}

	public static Map<String, String> getNameMap() {
		return nameMap;
	}

	public static void addNameSpace(String key, String value) {
		nameMap.put(key, value);
	}

}
