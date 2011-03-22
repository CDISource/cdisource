package org.cdiadvocate.testing;

import javax.inject.Inject;
import javax.inject.Named;

@Named("simpleBean")
public class SimpleBean {

	@Inject
	private InjectedBean injectedBean;
	
	@Inject
	private SingletonBean singletonBean;
	
	public InjectedBean getInjectedBean() {
		return injectedBean;
	}
	
	public SingletonBean getSingletonBean() {
		return singletonBean;
	}
	
}
