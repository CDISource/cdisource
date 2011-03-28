package org.cdisource.logging;

import java.io.Serializable;
import java.util.logging.Level;

public class JDKLogger implements Logger, Serializable{
	java.util.logging.Logger delegate;
	
	public JDKLogger() {
		
	}
	
	public JDKLogger(java.util.logging.Logger logger){
		this.delegate = logger;
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void severe(String msg, Object... args) {
		if (delegate.getLevel().intValue()>=Level.SEVERE.intValue()) {
			delegate.severe(String.format(msg, args));
		}
	}

	@Override
	public void warning(String msg, Object... args) {
		if (delegate.getLevel().intValue() >= Level.WARNING.intValue()) {
			delegate.warning(String.format(msg, args));
		}
	}

	@Override
	public void info(String msg, Object... args) {
		if (delegate.getLevel().intValue() >= Level.INFO.intValue()) {
			delegate.info(String.format(msg, args));
		}
	}

	@Override
	public void config(String msg, Object... args) {
		if (delegate.getLevel().intValue()>=Level.CONFIG.intValue()) {
			delegate.config(String.format(msg, args));
		}
	}

	@Override
	public void debug(String msg, Object... args) {
		if (delegate.getLevel().intValue()>=Level.FINE.intValue()) {
			delegate.fine(String.format(msg, args));
		}

	}

	@Override
	public void trace(String msg, Object... args) {
		if (delegate.getLevel().intValue()>=Level.FINER.intValue()) {
			delegate.finer(String.format(msg, args));
		}
	}

	@Override
	public void finest(String msg, Object... args) {
		if (delegate.getLevel().intValue()>=Level.FINEST.intValue()) {
			delegate.finest(String.format(msg, args));
		}
	}

	public boolean isLevel(LogLevel level) {
		switch (level) {
		case SEVERE:
			return delegate.isLoggable(Level.SEVERE);
		case WARNING:
			return delegate.isLoggable(Level.WARNING);
		case INFO:
			return delegate.isLoggable(Level.INFO);
		case CONFIG:
			return delegate.isLoggable(Level.CONFIG);
		case DEBUG:
			return delegate.isLoggable(Level.FINE);
		case TRACE:
			return delegate.isLoggable(Level.FINER);
		case FINEST:
			return delegate.isLoggable(Level.FINEST);
		 default:
			 throw new IllegalStateException("Unknown log level");
		}
	}
}
