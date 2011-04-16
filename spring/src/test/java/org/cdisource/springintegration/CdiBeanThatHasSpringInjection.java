package org.cdisource.springintegration;

import javax.inject.Inject;

public class CdiBeanThatHasSpringInjection {
	@Inject @Spring(name="fooBar") //Works with Weld and Candi but not OWB
	FooSpringBean springBean;
	@Inject @Spring(name="fooBarnotActuallyThere", required=false) //Works with Weld and Candi but not OWB
	FooSpringBean notActuallyThere;
	
	
	@Inject @Spring(type=FooSpringBean2.class) //Does not work in Weld but does work with CANDI
	FooSpringBean2 injectByType;
	/* The above causes weld to fail throughout. 
	 * 
	 * Exception 0 :
org.jboss.weld.exceptions.DefinitionException: WELD-001508 Cannot create an InjectionTarget from public abstract interface class org.cdisource.springintegration.FooSpringBean2 as it is an interface
	at org.jboss.weld.manager.SimpleInjectionTarget.<init>(SimpleInjectionTarget.java:71)
	at org.jboss.weld.manager.BeanManagerImpl.createInjectionTarget(BeanManagerImpl.java:1051)
	at org.cdisource.springintegration.SpringIntegrationExtention$SpringBean.<init>(SpringIntegrationExtention.java:93)
	at org.cdisource.springintegration.SpringIntegrationExtention.processInjectionTarget(SpringIntegrationExtention.java:48)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:39)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:25)
	at java.lang.reflect.Method.invoke(Method.java:597)
	 * 
	 * */

	
	public void validate () {
		if (springBean==null) {
			throw new IllegalStateException("spring bean was null");
		}
		if (notActuallyThere!=null) {
			throw new IllegalStateException("notActuallyThere should be null");
		}
		if (injectByType==null) {
			throw new IllegalStateException("injectByType should be there");
		}

	}
}
