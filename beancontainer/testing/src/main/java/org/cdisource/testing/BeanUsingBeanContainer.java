package org.cdisource.testing;

import javax.inject.Inject;

import org.cdisource.beancontainer.BeanContainer;

public class BeanUsingBeanContainer {
    @Inject BeanContainer container;
    
    public String foo () {
        return container.getBeanByType(InjectedBean.class).foo();
    }
}
