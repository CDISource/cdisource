package org.cdisource.logging;

public interface LogFactory {
	Logger getLogger(String string);
	Logger getLogger(Class<?> clazz);
}
