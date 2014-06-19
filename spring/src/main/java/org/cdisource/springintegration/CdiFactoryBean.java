package org.cdisource.springintegration;


import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.inject.spi.BeanManager;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerImpl;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class CdiFactoryBean implements FactoryBean<Object>, InitializingBean {

	private Class<?> beanClass;
	private boolean singleton = true;
	private BeanContainer beanContainer;
	private BeanManager beanManager;
	private Set<Annotation> qualifiers;

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
		return beanContainer.getBeanByType(beanClass, qualifiers.toArray(new Annotation[]{}));
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

	public void setQualifiers(Set<Annotation> qualifiers) {
		this.qualifiers = qualifiers;
	}
}
