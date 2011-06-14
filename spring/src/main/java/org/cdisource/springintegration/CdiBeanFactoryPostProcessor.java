package org.cdisource.springintegration;

import java.lang.reflect.Type;
import java.util.Set;

import javax.enterprise.inject.spi.Bean;

import org.cdisource.logging.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import static org.cdisource.logging.LogFactoryManager.logger;

public class CdiBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	
	private static Logger logger = logger(CdiBeanFactoryPostProcessor.class);
	
	private boolean useLongName;

	private BeanManagerLocationUtil beanManagerLocationUtil = new BeanManagerLocationUtil();


	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
		DefaultListableBeanFactory factory = (DefaultListableBeanFactory) beanFactory;
		
		Set<Bean<?>> beans = beanManagerLocationUtil.beanManager().getBeans(Object.class);
		for (Bean<?> bean : beans) {
			if (bean instanceof SpringIntegrationExtention.SpringBean) {
				continue;
			}
			
			if (bean.getName()!=null && bean.getName().equals("Spring Injection")){
				continue;
			}
			logger.debug("bean types = {}", bean.getTypes());
			Class<?> beanClass = getBeanClass(bean);
			BeanDefinitionBuilder definition = BeanDefinitionBuilder.rootBeanDefinition(CdiFactoryBean.class)
						.addPropertyValue("beanClass", beanClass)
						.addPropertyValue("beanManager", beanManagerLocationUtil.beanManager())
						.addPropertyValue("qualifiers", bean.getQualifiers())
						.setLazyInit(true);
			String name = generateName(bean);
			factory.registerBeanDefinition(name, definition.getBeanDefinition());
			logger.debug("bean name = {}, bean class = {}", bean.getName(), beanClass.getName());
		}
	}

	private Class<?> getBeanClass(Bean<?> bean) {
		Class<?> klass = Object.class;
		for (Type type : bean.getTypes()) {
			if (type instanceof Class) {
				Class<?> currentClass = (Class<?>) type;
				if (klass.isAssignableFrom(currentClass)) {
					klass = currentClass;
				}
			}
		}
		return klass;
	}

	private String generateName(Bean<?> bean) {
		String name = bean.getName() != null ? bean.getName() : generateNameBasedOnClassName(bean);
		return name;
	}

	private String generateNameBasedOnClassName(Bean<?> bean) {
		Class<?> beanClass = getBeanClass(bean);
		return !useLongName ? beanClass.getSimpleName() + "FactoryBean" : beanClass.getName().replace(".", "_") + "FactoryBean";
	}

	
	
	public void setUseLongName(boolean useLongName) {
		this.useLongName = useLongName;
	}


}
