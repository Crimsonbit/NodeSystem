package at.crimsonbit.nodesystem.language;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LanguageFileReader {

	private int state = 0;

	public LanguageFile readLanguageFile(InputStream inStream) {

		LanguageFile langFile = new LanguageFile();

		try (BufferedReader br = new BufferedReader(new InputStreamReader(inStream))) {
			String line;
			while ((line = br.readLine()) != null) {
				readLine(line, langFile);
				// System.out.println(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		state = 0;
		return langFile;
	}

	public LanguageFile readLanguageFile(File file) {
		FileReader reader = null;
		LanguageFile langFile = new LanguageFile();

		try {

			reader = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (reader != null) {

			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line;
				while ((line = br.readLine()) != null) {
					readLine(line, langFile);
					// System.out.println(line);
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		state = 0;
		return langFile;
	}

	private void processLine(String line, LanguageFile langFile) {

		if (LanguageHelp.checkForString(line)) {
			String[] l = line.split(LanguageHelp.STRING_FILE_DELIMITER);
			String token = l[0];
			String st = l[1];

			processString(token, st, langFile);
		}
	}

	private void processString(String tk, String st, LanguageFile langFile) {
		if (LanguageHelp.contains(st, LanguageHelp.STRING_FILE_LINE_END)) {
			st = st.split(LanguageHelp.STRING_FILE_STRING)[1];
			st = st.split(LanguageHelp.STRING_FILE_STRING)[0];

			if (LanguageHelp.compare(tk, LanguageHelp.STRING_FILE_AUTHOR))
				langFile.setAuthor(st);
			else if (LanguageHelp.compare(tk, LanguageHelp.STRING_FILE_LANGUAGE))
				langFile.setLang(st);
			else if (LanguageHelp.compare(tk, LanguageHelp.STRING_FILE_TYPE))
				langFile.setType(st);
			else {
				langFile.addString((tk), st);
			}

		} else {
			System.err.println("Line has no end symbol!");
		}
	}

	private void readLine(String line, LanguageFile langFile) {

		if (state == 2 || state == 4) {

			processLine(line, langFile);
		}

		if (LanguageHelp.contains(line, LanguageHelp.STRING_FILE_INFO) && state == 0) {
			state++;
		}
		if (LanguageHelp.contains(line, LanguageHelp.STRING_FILE_BLOCK_END)) {
			state++;
		}

		if (LanguageHelp.contains(line, LanguageHelp.STRING_FILE_BLOCK_START) && state == 1) {
			state++;
		}

		if (LanguageHelp.contains(line, LanguageHelp.STRING_FILE_STRINGS) && state == 2) {
			state++;
		}

		if (LanguageHelp.contains(line, LanguageHelp.STRING_FILE_BLOCK_START) && state == 3) {
			state++;
		}

		if (LanguageHelp.contains(line, LanguageHelp.STRING_FILE_BLOCK_END) && state == 4) {
			state++;

		}

	}

}
