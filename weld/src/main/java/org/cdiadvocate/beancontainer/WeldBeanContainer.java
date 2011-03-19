package org.cdiadvocate.beancontainer;

import java.lang.annotation.Annotation;

import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;


public class WeldBeanContainer implements BeanContainer {

    WeldContainer delegate;
    
    public Object getBeanByName(String name) {
	
	BeanManager beanManager = delegate.getBeanManager();
	Set<Bean<?>> beans = beanManager.getBeans(name);
	Bean<?>  bean = beanManager.resolve(beans);
	CreationalContext<?> context = beanManager.createCreationalContext(bean);
	
	return beanManager.getReference(bean, bean.getBeanClass(), context);
    }

    public <T> T getBeanByType(Class<T> type, Annotation... qualifiers) {
	return (T) delegate.instance().select(type, qualifiers).get();
    }

    @Override
	public void start() {
	delegate = new Weld().initialize();
    }
    
    @Override
	public void stop() {
        new Weld().shutdown();
    }
    

}
