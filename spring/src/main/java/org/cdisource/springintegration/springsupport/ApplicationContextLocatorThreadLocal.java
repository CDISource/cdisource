package org.cdisource.springintegration.springsupport;

import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.context.ApplicationContext;

public class ApplicationContextLocatorThreadLocal implements ApplicationContextLocator{

//	public static Map<ClassLoader, ThreadLocal<ApplicationContext>> map = new WeakHashMap<ClassLoader, ThreadLocal<ApplicationContext>>();
//
//	public static void putContext(ApplicationContext context) {
//		
//		synchronized (map) {
//			ThreadLocal<ApplicationContext> threadLocal = map.get(Thread.currentThread().getContextClassLoader());
//			if (threadLocal == null) {
//				threadLocal = new ThreadLocal<ApplicationContext>();
//				map.put(Thread.currentThread().getContextClassLoader(), threadLocal);
//			}
//			threadLocal.set(context);	
//		}
//	}
//
//	@Override
//	public ApplicationContext locateApplicationContext() {
//		synchronized (map) {
//			ThreadLocal<ApplicationContext> threadLocal = map.get(Thread.currentThread().getContextClassLoader());
//			if (threadLocal!=null) {
//				return threadLocal.get();
//			} else {
//				return null;
//			}
//		}
//	}

		public static ThreadLocal<ApplicationContext> tl = new ThreadLocal<ApplicationContext>();
	
		public static void putContext(ApplicationContext context) {
			tl.set(context);
		}
	
		@Override
		public ApplicationContext locateApplicationContext() {
			return tl.get();
		}	
	
}
