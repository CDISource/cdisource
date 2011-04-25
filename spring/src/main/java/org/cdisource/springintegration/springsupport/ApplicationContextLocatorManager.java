package org.cdisource.springintegration.springsupport;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.WeakHashMap;
import java.lang.ref.WeakReference;

public class ApplicationContextLocatorManager {

	public static Map<ClassLoader, WeakReference<ApplicationContextLocator>> map = Collections
			.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<ApplicationContextLocator>>());

	

	public static ApplicationContextLocator getInstance() {
		Properties properties = System.getProperties();
		return getInstance(properties);
	}
	
	public static ApplicationContextLocator getInstance(Properties properties) {
		ApplicationContextLocator applicationContextLocator = null;

		synchronized (map) { //synced so two threads will not try to put an entry in. First thread blocks until key is populated
			WeakReference<ApplicationContextLocator> weakReference = map
					.get(Thread.currentThread().getContextClassLoader());
			if (weakReference == null || weakReference.get() == null) {
				applicationContextLocator = locateAppContextLocator(properties);
			} else {
				applicationContextLocator = weakReference.get();
			}

		}
		return applicationContextLocator;
	}

	private static ApplicationContextLocator locateAppContextLocator(Properties properties) {
		ApplicationContextLocator applicationContextLocator = null;
				
		if (properties.getProperty(ApplicationContextLocator.class.getName())==null) {
			
			ServiceLoader<ApplicationContextLocator> instances = ServiceLoader.load(ApplicationContextLocator.class);
			Iterator<ApplicationContextLocator> iterator = instances.iterator();
			if (iterator.hasNext()) {
				applicationContextLocator = instances.iterator().next();
				
				if (iterator.hasNext()) {
					throw new IllegalStateException("There is more than one instance of " + ApplicationContextLocator.class.getName());
				}
			} else {
				applicationContextLocator = new ApplicationContextLocatorImpl();
			}
		} else {
			applicationContextLocator = instantiateUsingReflection(properties,
					applicationContextLocator); 
		}
		
		map.put(Thread.currentThread().getContextClassLoader(),
				new WeakReference<ApplicationContextLocator>(
						applicationContextLocator));
		return applicationContextLocator;
	}

	private static ApplicationContextLocator instantiateUsingReflection(
			Properties properties,
			ApplicationContextLocator applicationContextLocator) {
		try {
			applicationContextLocator = (ApplicationContextLocator) Class.forName(properties.getProperty(ApplicationContextLocator.class.getName())).newInstance();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return applicationContextLocator;
	}

}
