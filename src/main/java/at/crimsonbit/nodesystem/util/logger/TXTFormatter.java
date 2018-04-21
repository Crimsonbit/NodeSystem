package at.crimsonbit.nodesystem.util.logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Text Formatter for the SystemLogger.
 * 
 * @author Florian Wagner
 *
 */
public class TXTFormatter extends Formatter {

	private final Date dat = new Date();

	@Override
	public String format(LogRecord record) {
		dat.setTime(record.getMillis());
		String message = formatMessage(record);
		String throwable = "";
		if (record.getThrown() != null) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			pw.println();
			record.getThrown().printStackTrace(pw);
			pw.close();
			throwable = sw.toString();
		}

		return new String("[" + dat + "][" + record.getLevel() + "]	" + message + throwable + System.lineSeparator());
	}

}
