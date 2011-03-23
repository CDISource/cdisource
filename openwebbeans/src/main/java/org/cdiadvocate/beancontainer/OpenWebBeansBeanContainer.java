package org.cdiadvocate.beancontainer;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.webbeans.lifecycle.LifecycleFactory;
import org.apache.webbeans.spi.ContainerLifecycle;

public class OpenWebBeansBeanContainer extends AbstractBeanContainer {

	private ContainerLifecycle lifecycle;
	private BeanManager beanManager;

	@Override
	protected BeanManager locateBeanManager() {
		return beanManager;
	}

	@Override
	protected boolean isInitialized() {
		return (lifecycle != null && beanManager != null);
	}

	@Override
	protected void doStart() {
		lifecycle = LifecycleFactory.getInstance().getLifecycle();
		try {
			lifecycle.startApplication(null);
			beanManager = lifecycle.getBeanManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doStop() {
		lifecycle.stopApplication(null);
	}
}
