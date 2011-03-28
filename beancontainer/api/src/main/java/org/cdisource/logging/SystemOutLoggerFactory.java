package org.cdisource.logging;

/**
 * Simple logger for using System.out without worrying about logging configuration.
 * @author Rick Hightower
 *
 *@see SystemOutLoggerFactory
 */
public class SystemOutLoggerFactory implements LogFactory {

	@Override
	public Logger getLogger(String name) {
		return new SystemOutLogger(name);
	}

	@Override
	public Logger getLogger(Class<?> clazz) {
		return new SystemOutLogger(clazz.getName());
	}

}
