package org.cdiadvocate.beancontainer;

import java.lang.annotation.Annotation;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;

public abstract class AbstractBeanContainer implements BeanContainer {

	@Override
	public Object getBeanByName(String name) {
		checkForInitialization();
		if (name == null) {
			throw new IllegalArgumentException("CDI Bean name cannot be null");
		}
		
		
		BeanManager beanManager = locateBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(name);
		if (beans.isEmpty()) {
			throw new BeanNotFoundException("Could not locate a bean with name "+name);
		}
		Bean<?> bean = beanManager.resolve(beans);
		CreationalContext<?> context = beanManager
				.createCreationalContext(bean);
		return beanManager.getReference(bean, bean.getBeanClass(), context);
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
			throw new BeanNotFoundException("Could not locate a bean of type "+type.getName());
		}
		Bean<?> bean = beanManager.resolve(beans);
		CreationalContext<?> context = beanManager
				.createCreationalContext(bean);
		@SuppressWarnings("unchecked")
		T result = (T) beanManager.getReference(bean, bean.getBeanClass(), context);
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
		doStart();
	}

	@Override
	public void stop() {
		if (!isInitialized()) {
			throw new IllegalStateException(
					"CDI Environment has not been initialized");
		}
		doStop();

	}

	/**
	 * Performs the initialization of the CDI environment. This method can
	 * assume that it has not already been initialized. Override to implement
	 * the initialization for a particular CDI implementation.
	 */
	protected abstract void doStart();

	/**
	 * Performs the shutdown of the CDI environment. This method can assume that
	 * it has already been initialized. Override to implement the shutdown for a
	 * particular CDI implementation.
	 */
	protected abstract void doStop();

}
