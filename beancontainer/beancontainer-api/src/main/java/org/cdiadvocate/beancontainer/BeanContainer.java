package org.cdiadvocate.beancontainer;

import java.lang.annotation.Annotation;


/**
 * BeanContainer interface that defines a common interface to CDI implementations.
 * This small abstraction allows us to easily plugin different CDI implementations
 * for unit testing and for Java SE applications.
 * 
 * 
 * @author Rick Hightower
 * 
 */
public interface BeanContainer {
    /**
     * Look up a bean by name. 
     * You should really consider using, getBeanByType since this is the type safe version.
     * @param name The name of the bean you want to look up.
     * @return The bean you looked up.
     */
    public Object getBeanByName (String name);
    /**
     * Look up a bean by name and pass its type to avoid casting. 
     * You should really consider using, getBeanByType since this is the type safe version.
     * @param name The name of the bean you want to look up.
     * @return The bean you looked up.
     */    
    public <T> T getBeanByName (Class<T> type, String name);
    /**
     * Look up a bean its type and a list of annotations. 
     * This is the preferred way to look up a bean.
     * @param type The bean type, can be a interface or a super class.
     * @param qualifiers List of annotation qualifiers to perform the search.
     * @return The bean you looked up.
     */
    public <T> T getBeanByType(Class<T> type, Annotation ...qualifiers);
    
    /** Start the bean container. Gives the container a chance to scan the classpath looking
     * for ./META-INF/beans.xml and setting up the beans to be vended.
     * <p/>Throws a
	 * {@link BeanContainerInitializationException} if there is a problem
	 * initializing. 
     * */
    public void start();
    /** Stops the bean container. Allows it to do clean up and call any needed callback methods, i.e., 
     * @PostConstruct on signletons*/
    public void stop();
}
