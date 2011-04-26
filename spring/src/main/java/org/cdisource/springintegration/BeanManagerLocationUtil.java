package org.cdisource.springintegration;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerManager;
import org.cdisource.beancontainer.BeanManagerLocator;

public class BeanManagerLocationUtil {

	private final String BEAN_MANAGER_LOCATION = "java:comp/BeanManager";
	private BeanManager beanManager;

	BeanManager beanManager() {
		if (lookupInJNDI() == null) {
			BeanContainer beanContainer = BeanContainerManager.getInstance();
			return ((BeanManagerLocator) beanContainer).getBeanManager();
		} else {
			return this.beanManager;
		}
	}

	private BeanManager lookupInJNDI() {
		if (beanManager == null) {
			try {
				InitialContext ic = new InitialContext();
				return (BeanManager) ic.lookup(BEAN_MANAGER_LOCATION);
			} catch (Exception e) {
				return null;
			}
		}
		return this.beanManager;
	}

}
