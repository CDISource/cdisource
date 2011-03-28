package org.cdisource.logging;

import java.io.Serializable;

import static org.cdisource.logging.LogLevel.*;

/**
 * Useful for when debugging. Sometimes getting log config to do what you want is hard.
 * This is as easy as putting the following two props on your command line:
 * <p>
 * </p>
 * 
 * -Dorg.cdisource.logging.LogLevel=DEBUG -Dorg.cdisource.logging.LogFactory=org.cdisource.logging.SystemOutLoggerFactory
 * <p>
 * The above is to set the level to DEBUG. The level defaults to INFO.
 * </p>
 * @see LogLevel
 * @author Rick Hightower
 *
 */
public class SystemOutLogger implements Logger, Serializable {

	private String name;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SystemOutLogger(String name) {
		this.name = name;
	}
	
	private LogLevel level() {
		String level = System.getProperty("org.cdisource.logging.LogLevel", "INFO");
		LogLevel logLevel = LogLevel.valueOf(level);
		return logLevel;
	}

	private void printMessage(LogLevel msgLevel, String msg, Object... args) {
		int level = level().ordinal();
		if (level >= msgLevel.ordinal()) {
			String message = String.format("%s %s %s", this.name, msgLevel.name(), String.format(msg, args));
			System.out.println(message);
		}
		
	}

	@Override
	public void severe(String msg, Object... args) {
		printMessage(SEVERE, msg, args);
	}

	@Override
	public void warning(String msg, Object... args) {
		printMessage(WARNING, msg, args);
	}

	@Override
	public void info(String msg, Object... args) {
		printMessage(INFO, msg, args);
	}


	@Override
	public void config(String msg, Object... args) {
		printMessage(CONFIG, msg, args);
	}

	@Override
	public void debug(String msg, Object... args) {
		printMessage(DEBUG, msg, args);
	}

	@Override
	public void trace(String msg, Object... args) {
		printMessage(TRACE, msg, args);
	}

	@Override
	public void finest(String msg, Object... args) {
		printMessage(FINEST, msg, args);
	}

	@Override
	public boolean isLevel(LogLevel level) {
		return level == level();
	}

}
