package org.cdiadvocate.beancontainer;

import javax.enterprise.inject.spi.BeanManager;

public class ResinBeanContainer extends AbstractBeanContainer {
	
	com.caucho.resin.ResinBeanContainer delegate;
	
	@Override
	protected BeanManager locateBeanManager() {
		return delegate.getCdiManager();
	}

	@Override
	protected boolean isInitialized() {
		return delegate != null;
	}

	@Override
	protected void doStart() {
		delegate = new com.caucho.resin.ResinBeanContainer();
		delegate.start();		
	}

	@Override
	protected void doStop() {
		delegate.close();		
	}
}
