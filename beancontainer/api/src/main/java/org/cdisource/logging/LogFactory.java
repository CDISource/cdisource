package org.cdisource.logging;

/**
 * LogFactory loads a logger.
 * @author Rick Hightower
 *
 */
public interface LogFactory {
	Logger getLogger(String string);
	Logger getLogger(Class<?> clazz);
}
