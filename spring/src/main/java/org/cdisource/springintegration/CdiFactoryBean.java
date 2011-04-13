package org.cdisource.springintegration;


import org.springframework.beans.factory.FactoryBean;
import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerImpl;

public class CdiFactoryBean implements FactoryBean<Object> {

	private BeanManagerLocationUtil beanManagerLocationUtil = new BeanManagerLocationUtil();	
	private Class<?> beanClass;
	private boolean singleton = true;

	
	public void setUseJNDI(boolean useJNDI) {
		this.beanManagerLocationUtil.setUseJNDI(useJNDI);
	}


	
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public Object getObject() throws Exception {
		return beanContainer().getBeanByType(beanClass);
	}

	BeanContainer beanContainer = null;
	
	private BeanContainer beanContainer() {
		if (beanContainer!=null) return beanContainer;
		
		beanContainer = new BeanContainerImpl(beanManagerLocationUtil.beanManager());
		return beanContainer;
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

}
