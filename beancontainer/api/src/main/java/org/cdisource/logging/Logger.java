package org.cdisource.logging;

/**
 * Our logging interface. Simple and direct.
 * @author Rick Hightower
 *
 */
public interface Logger {
	void severe (String msg, Object... args);
	void warning (String msg, Object... args);
	void info (String msg, Object... args);
	void config(String msg, Object... args);
	void debug(String msg, Object... args);
	void trace(String msg, Object... args);
	void finest(String msg, Object... args);
	boolean isLevel(LogLevel level);
	
}
