package org.cdisource.beancontainer;

import javax.enterprise.inject.spi.BeanManager;

public interface BeanManagerLocator {
	/**
	 * Returns the underlying {@link BeanManager} instance for this
	 * implementation of CDI.
	 * 
	 * @return Instance of the {@link BeanManager} implemented by this CDI
	 *         implementation.
	 */
	BeanManager getBeanManager();

}
