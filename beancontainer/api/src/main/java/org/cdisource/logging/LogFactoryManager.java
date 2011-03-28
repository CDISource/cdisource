package org.cdisource.logging;
import java.util.Properties;
import java.util.ServiceLoader;

import org.cdisource.beancontainer.BeanContainer;

public class LogFactoryManager {
	public static String PROP_NAME = "org.cdisource.logging.LogFactory";

	private static LogFactory logFactory;
	
	/**
	 * Get the log factory instance using system properties. If this method
	 * is called before one of the initialize methods is called, then the
	 * manager creates and initializes a {@link LogFactory} instance to
	 * return.
	 * <p/>
	 * Auto initializing this way means that we can call the getter from
	 * anywhere without being totally concerned about who or where the container
	 * is initialized.
	 * 
	 * @see LogFactoryManager.PROP_NAME
	 * @see {@link LogFactoryManager#initialize()}
	 * */
	public static LogFactory getInstance() {
		if (logFactory == null) {
			initialize();
		}
		return logFactory;
	}
	
	public static Logger logger(String name) {
		return getInstance().getLogger(name);
	}

	public static Logger logger(Class<?> clazz) {
		return getInstance().getLogger(clazz);
	}

	/**
	 * Thread safe method to create and initialize the {@link LogFactory}
	 * implementation on the classpath.
	 * 
	 */
	public static void initialize() {
		initialize(System.getProperties());
	}
	
	
	/**
	 * Thread safe method to create and initialize the {@link LogFactory}
	 * implementation on the classpath.
	 * 
	 * @param properties
	 *            Properties to use for initialization
	 */
	public synchronized static void initialize(Properties properties) {
		if (logFactory != null) {
			return;
		}
		startUpInstance(properties);

	}

	/**
	 * Thread safe method to create a new instance of a {@link LogFactory}
	 * and start it up and make it available through the
	 * <code>getInstance()</code> method.
	 * 
	 * @param properties
	 *            Properties to use for initialization
	 */
	private synchronized static void startUpInstance(Properties properties) {
		// double check that the instance is null, someone might have created
		// it while we were entering this method.
		if (logFactory != null) {
			return;
		}
		logFactory = generateInstance(properties);
	}

	/**
	 * Internal method to create a new instance of a {@link LogFactory}
	 * implementation. The returned container would not have been initialized
	 * yet.
	 * 
	 * @param properties
	 *            Properties to use for initialization
	 * @return new instance of a {@link BeanContainer} implementation which has
	 *         not yet been started.
	 */
	private static LogFactory generateInstance(Properties properties) {
		try {
			/* The property should override the ServiceLoader if found. */
			String logFactoryClassname = properties.getProperty(PROP_NAME);

			/* If the property was not found, use the service loader. */
			if (logFactoryClassname == null) {
				ServiceLoader<LogFactory> instances = ServiceLoader.load(LogFactory.class);
				if (instances.iterator().hasNext()) {
					return instances.iterator().next();
				}
			}

			if (logFactoryClassname == null) {
				throw new IllegalStateException("Unable to find the logfactory," +
						" this should never happen because we always ship with a log factory");
			}

			/*
			 * Get the classloader associated with the current webapp and not
			 * the global classloader
			 */
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			Class<?> clazz = Class.forName(logFactoryClassname, true, contextClassLoader);
			return (LogFactory) clazz.newInstance();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
