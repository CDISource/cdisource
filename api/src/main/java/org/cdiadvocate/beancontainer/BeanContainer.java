package org.cdiadvocate.beancontainer;

import java.lang.annotation.Annotation;

public interface BeanContainer {
    public Object getBeanByName (String name);
    public <T> T getBeanByType(Class<T> type, Annotation ...qualifiers);
    public void start();
    public void stop();
    
}
