package org.cdisource.beancontainer;

import javax.enterprise.inject.spi.BeanManager;

import org.apache.webbeans.spi.ContainerLifecycle;

import org.apache.webbeans.config.WebBeansContext;

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
	protected void doStart() throws Exception {
		lifecycle = WebBeansContext.getInstance().getService(ContainerLifecycle.class);
		lifecycle.startApplication(null);
		beanManager = lifecycle.getBeanManager();
	}

	@Override
	protected void doStop() {
		lifecycle.stopApplication(null);
	}

}
