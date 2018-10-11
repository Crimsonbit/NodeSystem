package at.crimsonbit.nodesystem.language;

import java.util.HashMap;
import java.util.Map;

public class LanguageFile {

	private String lang;
	private String author;
	private String type;
	private Map<String, String> strings = new HashMap<String, String>();

	public LanguageFile(String type, String lang, String author, Map<String, String> strings) {
		super();
		this.type = type;
		this.lang = lang;
		this.author = author;
		this.strings = strings;
	}

	public LanguageFile() {
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Map<String, String> getStrings() {
		return strings;
	}

	public void setStrings(Map<String, String> strings) {
		this.strings = strings;
	}

	public void addString(String tk, String st) {
		this.strings.put(tk, st);
	}

	public String getString(String tk) {
		return this.strings.get(tk);
	}
	
	
	@Override
	public String toString() {
		return "LanguageFile [type= " + type + ", lang=" + lang + ", author=" + author + ", strings=" + strings + "]";
	}

}
