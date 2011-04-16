package org.cdisource.springintegration.springsupport;

import java.util.WeakHashMap;
import java.util.Map;

public class ApplicationContextLocatorManager {

	public static Map<ClassLoader, ApplicationContextLocator> map = new WeakHashMap<ClassLoader, ApplicationContextLocator>();
	
	public static ApplicationContextLocator getInstance() {
		ApplicationContextLocator applicationContextLocator = null;
		synchronized (map) {
			applicationContextLocator = map.get(Thread.currentThread().getContextClassLoader());
			if (applicationContextLocator==null) {
				applicationContextLocator = new ApplicationContextLocatorThreadLocal();
				map.put(Thread.currentThread().getContextClassLoader(), applicationContextLocator);
			}					
		}
		return applicationContextLocator;
	}

}
