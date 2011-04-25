package org.cdisource.springintegration.springsupport;


import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.context.ApplicationContext;


public class ApplicationContextLocatorImpl implements ApplicationContextLocator{

	public static Map<ClassLoader, WeakReference<ApplicationContext>> map = 
		
		Collections.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<ApplicationContext>>());

	public static void putContext(ApplicationContext context) {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			map.put(contextClassLoader, new WeakReference<ApplicationContext>(context));
	}

	@Override
	public ApplicationContext locateApplicationContext() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		WeakReference<ApplicationContext> reference = map.get(contextClassLoader);
		
		if (reference==null) {
			return null;
		} else {
			return reference.get();
		}
		
	}
	
}
