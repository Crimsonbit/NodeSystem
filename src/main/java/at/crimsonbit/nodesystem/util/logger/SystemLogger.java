package at.crimsonbit.nodesystem.util.logger;

import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger Utility for the NodeSystem.
 * 
 * @author Florian Wagner
 *
 */
public class SystemLogger {

	private static FileHandler fileTxt;
	private static TXTFormatter txtFormatter;
	private static FileHandler fileHTML;
	private static Formatter htmlFormatter;
	private static Logger logger;

	private static final String SUFFIX_TXT = ".txt";
	private static final String SUFFIX_HTML = ".html";

	public static void setup() throws IOException {

		logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

		Logger rootLogger = Logger.getLogger("");
		Handler[] handlers = rootLogger.getHandlers();
		if (handlers[0] instanceof ConsoleHandler) {
			rootLogger.removeHandler(handlers[0]);
		}

		logger.setLevel(Level.INFO);

		fileTxt = new FileHandler("Logging" + SUFFIX_TXT);
		fileHTML = new FileHandler("Logging" + SUFFIX_HTML);

		txtFormatter = new TXTFormatter();
		fileTxt.setFormatter(txtFormatter);
		logger.addHandler(fileTxt);

		htmlFormatter = new HTMLFormatter();
		fileHTML.setFormatter(htmlFormatter);
		logger.addHandler(fileHTML);
	}

	public static Logger getLogger() throws IOException {
		if (logger == null) {
			setup();
		}
		return logger;
	}

}
