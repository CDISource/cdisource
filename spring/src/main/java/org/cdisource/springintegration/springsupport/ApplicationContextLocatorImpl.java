package org.cdisource.springintegration.springsupport;


import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;


/** Simple implementation of the ApplicationContextLocator that is web application friendly.
 * It does not use a normal singleton design pattern. It uses a variant that will allow the webapplication to be relaoded.
 * 
 * @author rick
 *
 */
public class ApplicationContextLocatorImpl implements ApplicationContextLocator{

	private static Map<ClassLoader, WeakReference<ApplicationContext>> map = 
		Collections.synchronizedMap(new WeakHashMap<ClassLoader, WeakReference<ApplicationContext>>());

	public static void putContext(ApplicationContext context) {
			ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
			map.put(contextClassLoader, new WeakReference<ApplicationContext>(context));
	}

	public ApplicationContextLocatorImpl() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		WeakReference<ApplicationContext> reference = map.get(contextClassLoader);

		/* To prevent GC from killing this until web app is gone. 
		 */
		ApplicationContext applicationContext;
		
		if (reference==null) {
			throw new IllegalStateException("1: Applicaiton context must be associated with the current classloader first");
		} else {
			applicationContext = reference.get();
		}
		
		if (applicationContext!=null) {
			String beanName = ApplicationContextLocator.class.getName();
			if (!applicationContext.containsBean(beanName)) {
				ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext)applicationContext).getBeanFactory();
				beanFactory.registerSingleton(beanName, this);
			}
		} else {
			throw new IllegalStateException("1: Applicaiton context must be associated with the current classloader first");
		}
		
	}
	
	@Override
	public ApplicationContext locateApplicationContext() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		WeakReference<ApplicationContext> reference = map.get(contextClassLoader);

		ApplicationContext applicationContext;
		
		if (reference==null) {
			return null;
		} else {
			applicationContext = reference.get();
		}
				
		return applicationContext;
	}
	
}
