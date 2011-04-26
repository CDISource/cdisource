package org.cdisource.springintegration;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerImpl;
import org.cdisource.beancontainer.BeanContainerInitializationException;
import org.cdisource.beancontainer.BeanManagerLocator;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

public class CdiBeanFactory extends DefaultListableBeanFactory {


	/**
	 * Creates a new bean factory adapter
	 */
	public CdiBeanFactory() {
	}

	/**
	 * Returns true for defined beans
	 */
	@Override
	public boolean containsBeanDefinition(String beanName) {
		BeanManager beanManager = ((BeanManagerLocator)beanContainer()).getBeanManager();
		Set<Bean<?>> beans = beanManager.getBeans(beanName);
		return beans.isEmpty();
	}

	/**
	 * Returns the named beans
	 */
	@Override
	public String[] getBeanDefinitionNames() {
		return super.getBeanDefinitionNames();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getBean(String name, Class requiredType, Object... args) {
		return beanContainer().getBeanByName(requiredType, name);
	}

	@Override
	public <T> T getBean(Class<T> type) {
		return beanContainer().getBeanByType(type);
	}

	private final String BEAN_MANAGER_LOCATION = "java:comp/BeanManager";

	private BeanContainer beanContainer;

	private BeanContainer beanContainer() {
		if (beanContainer == null) {
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
				beanContainer = new BeanContainerImpl((BeanManager) bean);
			} else {
				String msg = "Looked up JNDI Bean is not a BeanManager instance, bean type is "
						+ bean.getClass().getName();
				throw new BeanContainerInitializationException(msg);
			}
		}
		return beanContainer;

	}

}
