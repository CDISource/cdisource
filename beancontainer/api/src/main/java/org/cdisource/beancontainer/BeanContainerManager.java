package org.cdisource.beancontainer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.WeakHashMap;

import org.cdisource.logging.Logger;


import static org.cdisource.logging.LogFactoryManager.logger;


/**
 * Thread safe Bean Container Manager that provides access to the CDI
 * implementation on the classpath by returning an instance of the
 * {@link BeanContainer} that represents that implementation.
 * <p>
 * You can swap out the BeanContainer just by including the BeanContainer jar
 * file for a specific implementation in your project.
 * </p>
 * <p>
 * We look for a file under classpath/META-INF/services called
 * org.cdisource.beancontainer.BeanContainer.
 * </p>
 * This file has one of three values:
 * <ol>
 * <li>org.cdisource.beancontainer.ResinBeanContainer</li>
 * <li>org.cdisource.beancontainer.OpenWebBeansBeanContainer</li>
 * <li>org.cdisource.beancontainer.WeldBeanContainer</li>
 * </ol>
 * 
 * 
 * <p>
 * You can also override which container is used using a System property
 * 
 * </p>
 * 
 * <code>$ java -Dorg.cdisource.beancontainer.BeanContainer=org.cdisource.beancontainer.ResinBeanContainer</code>
 * 
 * <p>
 * </p>
 * 
 * @see java.util.ServiceLoader.
 * 
 * @author Rick Hightower
 * @author Andy Gibson
 * 
 */
public class BeanContainerManager {

	/** Logger */
	private static Logger log = logger(BeanContainerManager.class);
	
	/** Property name that we use to look up the bean container override. */
	public static String PROP_NAME = "org.cdisource.beancontainer.BeanContainer";

	private static Map<ClassLoader, WeakReference<BeanContainer>> map = Collections
	.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<BeanContainer>>());
	
	private static boolean useStatic = false;
	private static BeanContainer beanContainer;
	
	public static void testEnv() {
		useStatic=true;
	}

	/**
	 * Thread safe method to create and initialize the {@link BeanContainer}
	 * implementation on the classpath.
	 * 
	 */
	public static void initialize() {
		log.trace("initialize() called using system properties");
		initialize(System.getProperties());
	}

	/**
	 * Thread safe method to create and initialize the {@link BeanContainer}
	 * implementation on the classpath.
	 * 
	 * @param properties
	 *            Properties to use for initialization
	 */
	public synchronized static void initialize(Properties properties) {
		log.trace("initialize(properties)");
		startUpInstance(properties);

	}

	/**
	 * Get the bean container instance using system properties. If this method
	 * is called before one of the initialize methods is called, then the
	 * manager creates and initializes a {@link BeanContainer} instance to
	 * return. This is good for calling from unit tests and/or standalone applications.
	 * It is not a good idea to use this from a web application due to classloader issues.
	 * <p/>
	 * Auto initializing this way means that we can call the getter from
	 * anywhere without being totally concerned about who or where the container
	 * is initialized.
	 * 
	 * @see BeanContainerManager.PROP_NAME
	 * @see {@link BeanContainerManager#initialize()}
	 * */
	public static BeanContainer getInstance() {
		
		if (useStatic && beanContainer!=null) {
			return beanContainer;
		}
		log.trace("getInstance() called");
		synchronized (map) {
			WeakReference<BeanContainer> weakReference = map.get(Thread.currentThread().getContextClassLoader());
			if (weakReference==null || weakReference.get()==null) {
				log.debug("getInstance():: instance was null");
				initialize();
			}
			return beanContainer();			
		}
	}

	private static BeanContainer beanContainer() {
		if (useStatic && beanContainer!=null) {
			return beanContainer;
		}
		WeakReference<BeanContainer> weakReference = map.get(Thread.currentThread().getContextClassLoader());
		if (weakReference== null || weakReference.get() == null) {
			throw new IllegalStateException("WeakReference<BeanContainer> is not set");
		}
		return weakReference.get();
	}


    /**
     * Create the bean container instance using system properties. If this method
     * is called before one of the initialize methods is called, then the
     * manager creates and initializes a {@link BeanContainer} instance to
     * return. This is good for calling from unit tests and/or standalone applications.
     * It is not a good idea to use this from a web application due to classloader issues.
     * <p/>
     * Auto initializing this way means that we can call the getter from
     * anywhere without being totally concerned about who or where the container
     * is initialized.
     * 
     * @see BeanContainerManager.PROP_NAME
     * @see {@link BeanContainerManager#initialize()}
     * */
    public static BeanContainer createInstance() {
        log.trace("createInstance() called");
        BeanContainer beanContainer = generateInstance(System.getProperties());
        log.debug("createInstance():: instance is null? %s", beanContainer == null ? "yes" : "no");
        return beanContainer;
    }
	/**
	 * Thread safe method to create a new instance of a {@link BeanContainer}
	 * and start it up and make it available through the
	 * <code>getInstance()</code> method.
	 * 
	 * @param properties
	 *            Properties to use for initialization
	 */
	private synchronized static void startUpInstance(Properties properties) {
		log.trace("startUpInstance(properties)");
		BeanContainer instance = generateInstance(properties);
		if (instance != null) {
			instance.start();
		}
		
		if (useStatic) {
			beanContainer= instance;
		} else {
			map.put(Thread.currentThread().getContextClassLoader(), new WeakReference<BeanContainer>(instance));
		}
	}

	/**
	 * Internal method to create a new instance of a {@link BeanContainer}
	 * implementation. The returned container would not have been initialized
	 * yet.
	 * 
	 * @param properties
	 *            Properties to use for initialization
	 * @return new instance of a {@link BeanContainer} implementation which has
	 *         not yet been started.
	 */
	private static BeanContainer generateInstance(Properties properties) {
		try {
			/* The property should override the ServiceLoader if found. */
			String beanContainerClassName = properties.getProperty(PROP_NAME);

			/* If the property was not found, use the service loader. */
			if (beanContainerClassName == null) {
				ServiceLoader<BeanContainer> instances = ServiceLoader.load(BeanContainer.class);
				if (instances.iterator().hasNext()) {
					return instances.iterator().next();

				}
			}

			if (beanContainerClassName == null) {
				StringWriter stringWriter = new StringWriter();
				PrintWriter out = new PrintWriter(stringWriter);
				out.println("Unable to find a BeanContainer on your classpath");
				out
						.println("Expecting to find beancontainer-weld-impl.jar that has org.cdisource.beancontainer.WeldBeanContainer.");
				out
						.println("OR, Expecting to find beancontainer-resin-impl.jar that has org.cdisource.beancontainer.ResinBeanContainer.");
				out
						.println("OR, Expecting to find beancontainer-openwebbeans-impl.jar that has org.cdisource.beancontainer.OpenWebBeansBeanContainer.");
				out
						.println("OR, Expecting to find some class that implements org.cdisource.beancontainer.BeanContainer on the classpath");
				out
						.println("with a META-INF/services/org.cdisource.beancontainer.BeanContainer file that has a single line which is the implementation.");
				out
						.println(" If this error message does not make sense refer to the JavaDoc for org.cdisource.beancontainer.BeanContainerManager.");
				throw new IllegalStateException(stringWriter.toString());
			}

			/*
			 * Get the classloader associated with the current webapp and not
			 * the global classloader
			 */
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			Class<?> clazz = Class.forName(beanContainerClassName, true, contextClassLoader);
			return (BeanContainer) clazz.newInstance();

		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Thread safe method to shutdown the {@link BeanContainer} instance and
	 * invalidate it. Further calls to get an instance or initialize will create
	 * a new instance.
	 */
	public static synchronized void shutdown() {
		BeanContainer beanContainer = beanContainer();
		if (beanContainer!=null) {
			beanContainer.stop();
		}
	}
}
