package org.cdisource.logging;

public class SystemOutLoggerFactory implements LogFactory {

	@Override
	public Logger getLogger(String name) {
		return new SystemOutLogger();
	}

	@Override
	public Logger getLogger(Class<?> clazz) {
		return new SystemOutLogger();
	}

}
