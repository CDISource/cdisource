package org.cdisource.beancontainer;

import java.lang.annotation.Annotation;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.cdisource.beancontainer.namespace.BeanNamespace;
//import javax.enterprise.context.spi.Context;


/**
 * BeanContainer interface that defines a common interface to CDI
 * implementations. This small abstraction allows us to easily plugin different
 * CDI implementations for unit testing and for Java SE applications.
 * 
 * 
 * @author Rick Hightower
 * 
 */
public interface BeanContainer {
	
//	public void registerContext(Context context, boolean replace);

	/**
	 * Look up a bean by name. You should really consider using, getBeanByType
	 * since this is the type safe version.
	 * 
	 * @param name
	 *            The name of the bean you want to look up.
	 * @return The bean you looked up.
	 */
	public Object getBeanByName(String name);

	/**
	 * Look up a bean by name and pass its type to avoid casting. You should
	 * really consider using, getBeanByType since this is the type safe version.
	 * 
	 * @param name
	 *            The name of the bean you want to look up.
	 * @return The bean you looked up.
	 */
	public <T> T getBeanByName(Class<T> type, String name);

	/**
	 * Look up a bean its type and a list of annotations. This is the preferred
	 * way to look up a bean.
	 * 
	 * @param type
	 *            The bean type, can be a interface or a super class.
	 * @param qualifiers
	 *            List of annotation qualifiers to perform the search.
	 * @return The bean you looked up.
	 */
	public <T> T getBeanByType(Class<T> type, Annotation... qualifiers);

	/**
	 * Start the bean container. Gives the container a chance to scan the
	 * classpath looking for ./META-INF/beans.xml and setting up the beans to be
	 * vended.
	 * <p/>
	 * Throws a {@link BeanContainerInitializationException} if there is a
	 * problem initializing.
	 * */
	public void start();

	/**
	 * Stops the bean container. Allows it to do clean up and call any needed
	 * callback methods, i.e.,
	 * 
	 * @PostConstruct on singletons
	 */
	public void stop();

	/**
	 * Returns the root instance of the {@link BeanNamespaceNotNeeded} object that can be
	 * used to look up {@link Bean} instances by name. Used for looking up beans
	 * that have longer namespaces such as "org.domain.project.name".
	 * Particularly useful for expression lookups when you only get the
	 * expression piece by piece.
	 * 
	 * @return A {@link BeanNamespaceNotNeeded} instance that can be used to lookup
	 *         beans.
	 */
	public BeanNamespace getBeanNamespace();

	/**
	 * Returns the underlying {@link BeanManager} instance for this
	 * implementation of CDI.
	 * 
	 * @return Instance of the {@link BeanManager} implemented by this CDI
	 *         implementation.
	 */
	public BeanManager getBeanManager();
	

}
