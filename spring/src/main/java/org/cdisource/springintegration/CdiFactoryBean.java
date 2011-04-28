package org.cdisource.springintegration;


import javax.enterprise.inject.spi.BeanManager;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerImpl;

public class CdiFactoryBean implements FactoryBean<Object>, InitializingBean {

	private Class<?> beanClass;
	private boolean singleton = true;
	private BeanContainer beanContainer;
	private BeanManager beanManager;
	

	@Override
	public void afterPropertiesSet() throws Exception {
		if (beanManager==null) throw new IllegalStateException("BeanManager must be set");
		beanContainer = new BeanContainerImpl(beanManager);
	}

	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public Object getObject() throws Exception {
		return beanContainer.getBeanByType(beanClass);
	}

	@Override
	public Class<?> getObjectType() {
		return beanClass;
	}

	@Override
	public boolean isSingleton() {
		return singleton;
	}

	public void setSingleton(boolean singleton) {
		this.singleton = singleton;
	}

	public void setBeanManager(BeanManager beanManager) {
		this.beanManager = beanManager;
	}

}
