package org.cdisource.springintegration;

import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerInitializationException;
import org.cdisource.beancontainer.BeanContainerManager;
import org.cdisource.beancontainer.BeanManagerLocator;

public class BeanManagerLocationUtil {
	
	private final String BEAN_MANAGER_LOCATION = "java:comp/BeanManager";
	private BeanManager beanManager;
	private boolean useJNDI = true;

	public void setUseJNDI(boolean useJNDI) {
		this.useJNDI = useJNDI;
	}


	BeanManager beanManager() {

			if (lookupInJNDI()==null) {
				BeanContainer beanContainer = BeanContainerManager.createInstance();
				beanContainer.start();
				return( (BeanManagerLocator)beanContainer).getBeanManager();
			} else {
				return this.beanManager;
			}
	
	}

	private BeanManager lookupInJNDI() {
		if (useJNDI==false) {
			return null;
		}
		if (beanManager == null) {
			Object bean = null;

			try {
				InitialContext ic = new InitialContext();
				bean = ic.lookup(BEAN_MANAGER_LOCATION);
			} catch (Exception e) {
				throw new BeanContainerInitializationException(
						"Unable to lookup BeanManager instance in JNDI", e);
			}
			if (bean == null) {
				throw new BeanContainerInitializationException(
						"Null value returned when looking up the BeanManager from JNDI");
			}
			if (bean instanceof BeanManager) {
				this.beanManager = (BeanManager) bean;
			} else {
				String msg = "Looked up JNDI Bean is not a BeanManager instance, bean type is "
						+ bean.getClass().getName();
				throw new BeanContainerInitializationException(msg);
			}
		}
		return this.beanManager;
	}


}
