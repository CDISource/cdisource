package org.cdisource.logging;

import java.io.Serializable;

import static org.cdisource.logging.LogLevel.*;

public class SystemOutLogger implements Logger, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void severe(String msg, Object... args) {
		System.out.printf("SEVERE: " + msg, args);
	}

	@Override
	public void warning(String msg, Object... args) {
		System.out.printf("WARNING: " + msg, args);
	}

	@Override
	public void info(String msg, Object... args) {
		LogLevel logLevel = level();
		if (logLevel == INFO || logLevel == SEVERE || logLevel == WARNING) {
			System.out.printf("INFO: " + msg + "\n", args);
		}
	}

	private LogLevel level() {
		String level = System.getProperty("org.cdisource.logging.LogLevel", "SEVERE");
		LogLevel logLevel = LogLevel.valueOf(level);
		return logLevel;
	}

	@Override
	public void config(String msg, Object... args) {
		LogLevel logLevel = level();
		if (logLevel == INFO || logLevel == SEVERE || logLevel == WARNING || logLevel == CONFIG) {
			System.out.printf("CONFIG: " + msg + "\n", args);
		}
	}

	@Override
	public void debug(String msg, Object... args) {
		LogLevel logLevel = level();
		if (logLevel == INFO || logLevel == SEVERE || logLevel == WARNING || logLevel == CONFIG || logLevel == DEBUG) {
			System.out.printf("DEBUG :" + msg + "\n", args);
		}
	}

	@Override
	public void trace(String msg, Object... args) {
		LogLevel logLevel = level();		
		if (logLevel == INFO || logLevel == SEVERE || logLevel == WARNING || 
				logLevel == CONFIG || logLevel == DEBUG ||  logLevel == TRACE) {
			System.out.printf("TRACE: " + msg + "\n", args);
		}
	}

	@Override
	public void finest(String msg, Object... args) {
		LogLevel logLevel = level();
		if (logLevel == INFO || logLevel == SEVERE || logLevel == WARNING || 
				logLevel == CONFIG || logLevel == DEBUG ||  logLevel == TRACE || logLevel == FINEST) {
			System.out.printf("FINEST: " + msg + "\n", args);
		}
	}

	@Override
	public boolean isLevel(LogLevel level) {
		return level == level();
	}

}
