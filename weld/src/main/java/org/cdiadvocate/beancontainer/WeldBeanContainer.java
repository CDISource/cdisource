package org.cdiadvocate.beancontainer;

import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class WeldBeanContainer extends AbstractBeanContainer {

	WeldContainer delegate;
	Weld weld;

	@Override
	protected void doStart() throws Exception {
		weld = new Weld();
		delegate = weld.initialize();
	}

	@Override
	protected void doStop() {
		weld.shutdown();		
	}

	@Override
	protected boolean isInitialized() {	
		return weld != null;
	}

	@Override
	protected BeanManager locateBeanManager() {
		return delegate.getBeanManager();		
	}
}
