package org.cdisource.springintegration;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.support.AbstractApplicationContext;

public class CdiApplicationContext extends AbstractApplicationContext {
	
	private final ConfigurableListableBeanFactory beanFactory
	    = new CdiBeanFactory();


	public CdiApplicationContext () {
		//refresh();
	}
	
	@Override
	protected void refreshBeanFactory() {
	}

	@Override
	protected void closeBeanFactory() {
	}

	@Override
	public ConfigurableListableBeanFactory getBeanFactory() {
		return beanFactory;
	}

}
