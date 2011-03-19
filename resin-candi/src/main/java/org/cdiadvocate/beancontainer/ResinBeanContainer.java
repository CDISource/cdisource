package org.cdiadvocate.beancontainer;

import java.lang.annotation.Annotation;

public class ResinBeanContainer implements BeanContainer {
    com.caucho.resin.ResinBeanContainer delegate = new com.caucho.resin.ResinBeanContainer();

    public Object getBeanByName(String name) {
        return delegate.getBeanByName(name);
    }

    public <T> T getBeanByType(Class<T> type, Annotation... qualifiers) {
        return delegate.getInstance(type, qualifiers);
    }
    @Override
    public void start() {
        delegate.start();
    }

    @Override
    public void stop() {
        delegate.close();
    }

}
