package org.cdisource.beancontainer;

import java.util.Properties;
import java.util.ServiceLoader;

/**
 * Manager that provides access to the CDI implementation on the classpath by
 * returning an instance of the {@link BeanContainer} that represents that
 * implementation.
 * <p>
 * You can swap out the BeanContainer just by including the right jar file
 * implementation in your project.
 * </p>
 * <p>
 * We look for a file under classpath/META-INF/services called
 * ororg.cdisource.container.BeanContainer.
 * </p>
 * This file has one of three values:
 * <ol>
 * <li>org.org.cdisource.ntainer.ResinBeanContainer</li>
 * <li>org.cdorg.cdisource.ainer.OpenWebBeansBeanContainer</li>
 * <li>org.cdisorg.cdisource.ner.WeldBeanContainer</li>
 * </ol>
 * 
 * 
 * <p>
 * You can also override which container is used using a System property
 * 
 * </p>
 * 
 * <code>$ java -Dorg.cdi.advocacy.BeanContainer=org.cdisouorg.cdisource.r.ResinBeanContainer</code>
 * 
 * <p>
 * </p>
 * 
 * @see java.util.ServiceLoader.
 * 
 * @author Rick Hightower
 * 
 */
public class BeanContainerManager {
	/** Property name that we use to look up the bean container override. */
	public static String PROP_NAME = "org.cdi.advocacy.BeanContainer";

	private static BeanContainer instance;

	public static void initialize() {
		initialize(System.getProperties());
	}

	public synchronized static void initialize(Properties properties) {
		if (instance != null) {
			return;
		}
		startUpInstance(properties);

	}

	/**
	 * Get the bean container instance using system properties.
	 * 
	 * @see BeanContainerManager.PROP_NAME
	 * */
	public static BeanContainer getInstance() {
		if (instance == null) {
			initialize();
		}
		return instance;
	}

	private synchronized static void startUpInstance(Properties properties) {
		// check once more that the instance is null, someone might have created
		// it while we were entering this method.
		if (instance != null) {
			return;
		}
		generateInstance(properties);
		if (instance != null) {
			instance.start();
		}
	}

	private synchronized static void generateInstance(Properties properties) {
		// check once more that the instance is null, someone might have created
		// it while we were entering this method. Since this is a private
		// method, we shouldn't need to worry about it
		if (instance != null) {
			return;
		}

		try {
			/* The property should override the ServiceLoader if found. */
			String beanContainerClassName = properties.getProperty(PROP_NAME);

			/* If the property was not found, use the service loader. */
			if (beanContainerClassName == null) {
				ServiceLoader<BeanContainer> instances = ServiceLoader
						.load(BeanContainer.class);
				if (instances.iterator().hasNext()) {
					instance = instances.iterator().next();
					return;
				}
			}

			/*
			 * If class property not found in the passed properties, then Resin
			 * is the default for now, we may switch to the RI.
			 */
			beanContainerClassName = beanContainerClassName != null ? beanContainerClassName
					: "org.cdisource.beancontainer.WeldBeanContainer";

			/*
			 * Get the classloader associated with the current webapp and not
			 * the global classloader
			 */
			ClassLoader contextClassLoader = Thread.currentThread()
					.getContextClassLoader();
			Class<?> clazz = Class.forName(beanContainerClassName, true,
					contextClassLoader);
			instance = (BeanContainer) clazz.newInstance();
			return;

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	public static synchronized void shutdown() {
		if (instance != null) {
			instance.stop();
			instance = null;
		}
	}
}
