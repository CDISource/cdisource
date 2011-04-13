package org.cdisource.beancontainer;

//import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

public class BeanContainerImpl extends AbstractBeanContainer {
    @Inject protected BeanManager manager;
    
    public BeanContainerImpl() {
    	
    }
    
    public BeanContainerImpl(BeanManager manager) {
    	this.manager = manager;
    }

    @Override
    protected BeanManager locateBeanManager() {
        return manager;
    }

    @Override
    protected boolean isInitialized() {
        return manager != null;
    }

    @Override
    protected void doStart() throws Exception {
    }

    @Override
    protected void doStop() {
    }

}
