package at.crimsonbit.nodesystem.language;

public class LanguageHelp {

	public static final String MODULES_LOCATION = "Modules/";
	public static final String STRING_FILE_INFO = "info";
	public static final String STRING_FILE_LANGUAGE = "language";
	public static final String STRING_FILE_TYPE = "type";
	public static final String STRING_FILE_AUTHOR = "author";
	public static final String STRING_FILE_STRINGS = "strings";
	public static final String STRING_FILE_DELIMITER = ":";
	public static final String STRING_FILE_LINE_END = ";";
	public static final String STRING_FILE_BLOCK_START = "{";
	public static final String STRING_FILE_BLOCK_END = "}";
	public static final String STRING_FILE_STRING = "\"";

	public static boolean compare(String s1, String s2) {
		if (s1 == s2 || s1.equals(s2))
			return true;
		return false;
	}

	public static boolean contains(String s1, String s2) {
		if (s1.contains(s2))
			return true;
		return false;
	}

	public static boolean checkForString(String line) {
		if (!contains(line, STRING_FILE_INFO) && !contains(line, STRING_FILE_STRINGS)
				&& !contains(line, STRING_FILE_BLOCK_START) && !contains(line, STRING_FILE_BLOCK_END)
				&& !line.equals("") && !line.equals(" "))
			return true;
		return false;
	}

}
