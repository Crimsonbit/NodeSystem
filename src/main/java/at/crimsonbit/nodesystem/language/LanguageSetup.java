package at.crimsonbit.nodesystem.language;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LanguageSetup {

	private String currentLanguage = "Deutsch";
	private LanguageFileReader reader = new LanguageFileReader();
	private static LanguageSetup instance;
	private List<LanguageFile> langFiles = new ArrayList<LanguageFile>();
	private Map<LangType, LanguageFile> langMap = new HashMap<>();

	public static LanguageSetup getInstance() {
		if (instance == null) {
			instance = new LanguageSetup();
		}
		return instance;
	}

	public static LanguageSetup getInstance(String lang) {
		if (instance == null) {
			instance = new LanguageSetup(lang);
		}
		return instance;
	}

	private LanguageSetup(String lang) {
		settLanguage(lang);
		readCoreLanguageFile();
	}

	private LanguageSetup() {
		readCoreLanguageFile();
	}

	private void readCoreLanguageFile() {
		LanguageFile f = reader.readLanguageFile(new File(getClass().getResource("en_core.strings").getFile()));
		langMap.put(new LangType(f.getType(), f.getLang()), f);
		f = reader.readLanguageFile(new File(getClass().getResource("de_core.strings").getFile()));
		langMap.put(new LangType(f.getType(), f.getLang()), f);

	}

	public String getLanguage() {
		return currentLanguage;
	}

	public void settLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}

	public String getString(String type, String token) {
		LanguageFile lang = langMap.get(new LangType(type, currentLanguage));
		if (lang != null) {
			String localized = lang.getString(token);
			if (localized != null)
				return localized;
		}
		return "STRING_NOT_FOUND";
	}

	public void readLanguageFile(InputStream inStream) {
		LanguageFile f = reader.readLanguageFile(inStream);
		langMap.put(new LangType(f.getType(), f.getLang()), f);

	}

	private static class LangType {
		public final String type;
		public final String lang;

		public LangType(String type, String lang) {
			super();
			this.type = type;
			this.lang = lang;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((lang == null) ? 0 : lang.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LangType other = (LangType) obj;
			if (lang == null) {
				if (other.lang != null)
					return false;
			} else if (!lang.equals(other.lang))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}

	}

}
