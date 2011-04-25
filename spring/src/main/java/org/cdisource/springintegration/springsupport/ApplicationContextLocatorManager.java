package org.cdisource.springintegration.springsupport;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.lang.ref.WeakReference;


/**
 * ApplicationContextLocatorManager, this is used to find an ApplicationContextLocator. 
 * An ApplicationContextLocator locates an application context.
 * This does not use a normal singleton. It uses a singleton that will not prevent a web application from reloading.
 * It does this by using WeakHashMap with WeakReference keyed on the current classloader.
 * 
 * To load the ApplicationContextLocator it first checks to see if a property or system property has been set called
 * org.cdisource.springintegration.springsupport.ApplicationContextLocator.
 * 
 * If this has not been set, then it checks the ServiceLoader. 
 * If more than one item is loaded form the ServiceLoader, it throws an exception.
 * 
 * If one ApplicationContextLocator is found with the ServiceLoader then it uses this ApplicationContextLocator.
 * 
 * Lastly, it instantiates a ApplicationContextLocatorImpl which is the default implementation.
 * 
 * @see ServiceLoader.
 * 
 * @author rick
 *
 */
public class ApplicationContextLocatorManager {

	/** Using a WeakHash map instead of a singleton to be classloader friendly in a Java EE application. 
	 * Essentially, I don't want to hold on to a class otherwise the container will not be able to reload the webapplication.
	 * */
	private static Map<ClassLoader, WeakReference<ApplicationContextLocator>> map = Collections
			.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<ApplicationContextLocator>>());

	
	/** Simple getInstance. Searches the System properties for service locator.
	 * If system property not found, it then uses ServiceLoader.
	 * 
	 * @see ApplicationContextLocatorManager
	 * @see ServiceLoader
	 * 
	 * */
	public static ApplicationContextLocator getInstance() {
		Properties properties = System.getProperties();
		return getInstance(properties);
	}
	
	/**
	 * Same as ApplicationContextLocator.getInstance except you can pass the properties instead of using system properties.
	 * @param properties
	 * @return
	 */
	public static ApplicationContextLocator getInstance(Properties properties) {
		ApplicationContextLocator applicationContextLocator = null;

		synchronized (map) { //synced so two threads will not try to put an entry in. First thread blocks until key is populated
			WeakReference<ApplicationContextLocator> weakReference = map
					.get(Thread.currentThread().getContextClassLoader());
			if (weakReference == null || weakReference.get() == null) {
				applicationContextLocator = locateAppContextLocator(properties);
				map.put(Thread.currentThread().getContextClassLoader(),
						new WeakReference<ApplicationContextLocator>(
								applicationContextLocator));
			} else {
				applicationContextLocator = weakReference.get();
			}

		}
		return applicationContextLocator;
	}

	/**
	 * Location logic. This is where all of the logic for location is implemented.
	 * @param properties
	 * @return
	 */
	private static ApplicationContextLocator locateAppContextLocator(Properties properties) {
		ApplicationContextLocator applicationContextLocator = null;
		
		/* See if the property is set. */
		if (properties.getProperty(ApplicationContextLocator.class.getName())==null) {
			
			/* if the property is not set, try to use the ServiceLoader to find the implementation. */
			ServiceLoader<ApplicationContextLocator> instances = ServiceLoader.load(ApplicationContextLocator.class);
			Iterator<ApplicationContextLocator> iterator = instances.iterator();
			
			/* A ServiceLoader can load more than one instance. We only need/want one. */
			/* See if we have one. */
			if (iterator.hasNext()) {
				
				/* If we have one, assign it. */
				applicationContextLocator = instances.iterator().next();
				
				/* If there is more than one, than somebody messed something up in their build. Let them know. */
				if (iterator.hasNext()) {
					throw new IllegalStateException("There is more than one instance of " + ApplicationContextLocator.class.getName());
				}
			} else {
				/* If one could not be found, go ahead and supply the default. */
				applicationContextLocator = new ApplicationContextLocatorImpl();
			}
		} else {
			/* If the property was found, then they want to use a specific implementation. 
			 * Instantiate this using reflection. */
			applicationContextLocator = instantiateUsingReflection(properties,
					applicationContextLocator); 
		}
		
		return applicationContextLocator;
	}

	/**
	 * Method that creates an instance of ApplicationContextLocator using the value of properties.get(ApplicationContextLocator.class.getName())
	 * @param properties
	 * @param applicationContextLocator
	 * @return
	 */
	private static ApplicationContextLocator instantiateUsingReflection(Properties properties,
			ApplicationContextLocator applicationContextLocator) {
		try {
			applicationContextLocator = (ApplicationContextLocator) Class.forName(properties.getProperty(ApplicationContextLocator.class.getName())).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return applicationContextLocator;
	}

}
