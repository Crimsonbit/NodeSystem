package at.crimsonbit.nodesystem.language;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LanguageSetup {

	private String currentLanguage = "Deutsch";
	private LanguageFileReader reader = new LanguageFileReader();
	private static LanguageSetup instance;
	private List<LanguageFile> langFiles = new ArrayList<LanguageFile>();

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
		langFiles.add(reader.readLanguageFile(new File(getClass().getResource("en_core.strings").getFile())));
		langFiles.add(reader.readLanguageFile(new File(getClass().getResource("de_core.strings").getFile())));

	}

	public String getLanguage() {
		return currentLanguage;
	}

	public void settLanguage(String currentLanguage) {
		this.currentLanguage = currentLanguage;
	}

	public String getString(String type, String token) {
		for (LanguageFile f : langFiles) {
			if (f.getType().equals(type)) {
				if (f.getLang().equals(currentLanguage)) {

					return f.getString((token));
				}
			}
		}
		return "STRING NOT FOUND";
	}
	
	public void readLanguageFile(InputStream inStream) {
		langFiles.add(reader.readLanguageFile(inStream));

	}

}
