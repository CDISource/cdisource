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
		if (this.beanManager!=null) {
			return this.beanManager;
		}
		if (lookupInJNDI() == null) {
			BeanContainer beanContainer = BeanContainerManager.getInstance();
			this.beanManager = ((BeanManagerLocator) beanContainer)
					.getBeanManager();
		}
		
		if (this.beanManager==null) {
			throw new IllegalStateException("BEAN MANAGER IS NULL");
		}
		return this.beanManager;

	}

	private BeanManager lookupInJNDI() {
		try {
			InitialContext ic = new InitialContext();
			return (this.beanManager = (BeanManager) ic.lookup(BEAN_MANAGER_LOCATION));
		} catch (Exception e) {// need to log this
			return null;
		}
	}

}
