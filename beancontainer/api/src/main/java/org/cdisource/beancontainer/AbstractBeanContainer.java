package org.cdisource.beancontainer;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

import org.cdisource.beancontainer.namespace.BeanNamespace;

/**
 * Abstract implementation of the {@link BeanContainer} interface that
 * implements the common lookup code once the container is initialized.
 * <p/>
 * Subclasses must implement the methods to actually startup and shutdown the
 * container as well as locating an instance of the {@link BeanManager}.
 * </p>
 * This class also handles exceptions for when an attempt is made to call a
 * method on an inititialized container.
 * 
 * @author Andy Gibson
 * @author Rick Hightower
 * 
 */
public abstract class AbstractBeanContainer implements BeanContainer {

	private BeanNamespace beanNamespace;

	@Override
	public Object getBeanByName(String name) {
		checkForInitialization();
		if (name == null) {
			throw new IllegalArgumentException("CDI Bean name cannot be null");
		}

		BeanManager beanManager = locateBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(name);
		if (beans.isEmpty()) {
			throw new BeanNotFoundException(
					"Could not locate a bean with name " + name);
		}
		Bean<?> bean = beanManager.resolve(beans);
		CreationalContext<?> context = beanManager
				.createCreationalContext(bean);
		return beanManager.getReference(bean, bean.getBeanClass(), context);
	}

	@Override
	public <T> T getBeanByName(Class<T> type, String name) {
		@SuppressWarnings("unchecked")
		T beanByName = (T) this.getBeanByName(name);
		return beanByName;
	}

	@Override
	public <T> T getBeanByType(Class<T> type, Annotation... qualifiers) {
		checkForInitialization();
		if (type == null) {
			throw new IllegalArgumentException("CDI Bean type cannot be null");
		}

		BeanManager beanManager = locateBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(type, qualifiers);
		if (beans.isEmpty()) {
			throw new BeanNotFoundException("Could not locate a bean of type "
					+ type.getName());
		}
		Bean<?> bean = beanManager.resolve(beans);
		CreationalContext<?> context = beanManager
				.createCreationalContext(bean);
		@SuppressWarnings("unchecked")
		T result = (T) beanManager.getReference(bean, bean.getBeanClass(),
				context);
		return result;
	}

	/**
	 * Checks to see if the container is initialized and if not, throws an
	 * exception for the user.
	 */
	protected void checkForInitialization() {
		if (!isInitialized()) {
			throw new IllegalStateException(
					"CDI environment has not been initialized through a call to start()");
		}
	}

	/**
	 * Returns an instance of the {@link BeanManager}. Override this method to
	 * check for initialization which will vary from implementation to
	 * implementation.
	 * 
	 * @return The bean manager instance
	 */
	protected abstract BeanManager locateBeanManager();
	
	@Override
	public BeanManager getBeanManager() {
		return locateBeanManager();
	}

	/**
	 * Returns true if and only if the CDI environment has been initialized and
	 * the {@link BeanContainer} is able to return a {@link BeanManager}
	 * instance. Override this method to determine whether the particular
	 * {@link BeanContainer} environment has been initialized.
	 * 
	 * @return Indicates whether the CDI environment has been initialized
	 */
	protected abstract boolean isInitialized();

	@Override
	public void start() {
		if (isInitialized()) {
			throw new IllegalStateException(
					"CDI Environment has already been initialized");
		}
		try {
			doStart();
		} catch (Exception e) {
			throw new BeanContainerInitializationException(
					"Unable to start BeanContainer : " + e.getMessage(), e);
		}

	}

	@Override
	public void stop() {
		if (!isInitialized()) {
			throw new IllegalStateException(
					"CDI Environment has not been initialized");
		}
		doStop();
	}

	@Override
	public BeanNamespace getBeanNamespace() {
		if (beanNamespace == null) {
			synchronized (this) {
				if (beanNamespace == null) {
					beanNamespace = buildNamespaceLookup();
				}
			}
		}

		return beanNamespace;
	}

	protected BeanNamespace buildNamespaceLookup() {
		BeanNamespace namespace = new BeanNamespace();
		BeanManager bm = locateBeanManager();
		for (Bean<?> bean : bm.getBeans(Object.class)) {
			String name = bean.getName();
			if (name != null && name.length() != 0) {
				namespace.addObject(name, bean);
			}
		}
		return namespace;
	}

	/**
	 * Performs the initialization of the CDI environment. This method can
	 * assume that it has not already been initialized. Override to implement
	 * the initialization for a particular CDI implementation.
	 */
	protected abstract void doStart() throws Exception;

	/**
	 * Performs the shutdown of the CDI environment. This method can assume that
	 * it has already been initialized. Override to implement the shutdown for a
	 * particular CDI implementation.
	 */
	protected abstract void doStop();

}
