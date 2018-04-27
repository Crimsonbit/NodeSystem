package at.crimsonbit.nodesystem.util.logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import at.crimsonbit.nodesystem.gui.GLogPane;

/**
 * <h1>SystemLogger</h1>
 * <p>
 * This is a utility logger class that is used by the whole NodeSystem.
 * </p>
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
	private static String logFileName = "logFile";
	private static final String SUFFIX_TXT = ".txt";
	private static final String SUFFIX_HTML = ".html";
	private static GLogPane loggingPane;
	private static final Date dat = new Date();
	private static boolean doLog = false;
	private static boolean isAttached = false;
	private static boolean useHTML = true;
	private static boolean useTXT = true;

	private static void createLogger() {
		try {
			setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Attaches the logger.
	 * 
	 * @param uH
	 *            use HTML logging
	 * @param uT
	 *            use TXT logging
	 */
	public static void attachLogger(boolean uH, boolean uT) {
		if (!isAttached) {
			doLog = true;
			isAttached = true;
			useHTML = uH;
			useTXT = uT;
			// createLogger();
		}
	}

	private static void setup() throws IOException {
		if (doLog && isAttached) {
			logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

			Logger rootLogger = Logger.getLogger("");
			Handler[] handlers = rootLogger.getHandlers();
			if (handlers[0] instanceof ConsoleHandler) {
				rootLogger.removeHandler(handlers[0]);
			}

			logger.setLevel(Level.INFO); // default level
			rootLogger.addHandler(new Handler() {

				@Override
				public void publish(LogRecord record) {

					dat.setTime(record.getMillis());
					TXTFormatter form = new TXTFormatter();
					String message = form.formatMessage(record);
					String throwable = "";
					if (record.getThrown() != null) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						pw.println();
						record.getThrown().printStackTrace(pw);
						pw.close();
						throwable = sw.toString();
					}

					if (loggingPane != null) {
						loggingPane.appendLog(new String("[" + dat + "][" + record.getLevel() + "] :	" + message
								+ throwable + System.lineSeparator()));
						// loggingPane.getChildren().get(0).relocate(0, c -= 31);

					}
				}

				@Override
				public void flush() {
					// TODO Auto-generated method stub

				}

				@Override
				public void close() throws SecurityException {
					// TODO Auto-generated method stub

				}

			});
			if (useTXT) {
				fileTxt = new FileHandler(logFileName + SUFFIX_TXT);
				txtFormatter = new TXTFormatter();
				fileTxt.setFormatter(txtFormatter);
				logger.addHandler(fileTxt);
			}
			if (useHTML) {
				fileHTML = new FileHandler(logFileName + SUFFIX_HTML);
				htmlFormatter = new HTMLFormatter();
				fileHTML.setFormatter(htmlFormatter);
				logger.addHandler(fileHTML);
			}
		}

	}

	public static Logger getLogger(GLogPane pane) throws IOException {
		if (logger == null && isAttached) {
			loggingPane = pane;
			createLogger();
		}
		return logger;
	}

}
