package org.cdisource.beancontainer;

//import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class WeldBeanContainer extends AbstractBeanContainer {

//	@Override
//	public void registerContext(Context context, boolean replace) {
//		throw new UnsupportedOperationException("TODO");
//	}

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
