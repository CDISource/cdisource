package org.cdisource.beancontainer;

//import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;

public class JndiBeanContainer extends AbstractBeanContainer {
	
	
	private BeanManager beanManager;

	private final String BEAN_MANAGER_LOCATION = "java:comp/BeanManager";

	@Override
	protected BeanManager locateBeanManager() {
		return beanManager;
	}

	@Override
	protected boolean isInitialized() {
		return beanManager != null;
	}

	@Override
	protected void doStart() throws Exception {
		// lookup the bean manager from jndi
		InitialContext ic = new InitialContext();
		Object bean = null;
		try {
			bean = ic.lookup(BEAN_MANAGER_LOCATION);
		} catch (Exception e) {
			throw new BeanContainerInitializationException("Unable to lookup BeanManager instance in JNDI", e);
		}
		if (bean == null) {
			throw new BeanContainerInitializationException(
					"Null value returned when looking up the BeanManager from JNDI");
		}
		if (bean instanceof BeanManager) {
			beanManager = (BeanManager) bean;
		} else {
			String msg = "Looked up JNDI Bean is not a BeanManager instance, bean type is " + bean.getClass().getName();
			throw new BeanContainerInitializationException(msg);
		}
	}

	@Override
	protected void doStop() {
		beanManager = null;
	}


}
