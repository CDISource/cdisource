package org.cdisource.beancontainer;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.AfterBeanDiscovery;

public class ScopeManager {
	private static ScopeManager instance = new ScopeManager();
	
	private Map<Class<? extends Annotation>, ScopeController> map = new HashMap<Class<? extends Annotation>, ScopeController>(7);

	public void stop(Class<? extends Annotation> scopeType) {
		ScopeController control = map.get(scopeType);
		if (control!=null) {
			control.stop();
		}
	}
	public static void startScope(Class<? extends Annotation> scopeType) {
		instance.start(scopeType);
	}
	public static void stopScope(Class<? extends Annotation> scopeType) {
		instance.stop(scopeType);
	}

	
	public void start(Class<? extends Annotation> scopeType) {
		ScopeController control = map.get(scopeType);
		if (control!=null) {
			control.start();
		}
	}
	
//	public void register(ScopeController control, Context context) {
//		BeanContainerManager.getInstance().registerContext(context, true);
//		map.put(context.getScope(), control);
//	}

	public void register(AfterBeanDiscovery abd, ScopeController control, Context context) {
		abd.addContext(context);
		map.put(context.getScope(), control);
	}

	public static ScopeManager getInstance() {
		return instance;
	}
	public static void reset() {
		instance.map.clear();
		instance = new ScopeManager();
	}

}
