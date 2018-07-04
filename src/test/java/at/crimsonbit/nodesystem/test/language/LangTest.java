package at.crimsonbit.nodesystem.test.language;

import java.io.File;

import at.crimsonbit.nodesystem.language.LanguageFile;
import at.crimsonbit.nodesystem.language.LanguageFileReader;

public class LangTest {

	public static void main(String[] args) {
		
		String file = "C:\\Users\\NEON\\Desktop\\langFile.txt";
		LanguageFileReader reader = new LanguageFileReader();
		
		LanguageFile langFile = reader.readLanguageFile(new File(file));
		System.out.println(langFile);

	}

}
