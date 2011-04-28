package org.cdisource.springintegration;

import java.util.Set;

import javax.enterprise.inject.spi.Bean;
import javax.inject.Named;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;


public class CdiBeanFactoryPostProcessor implements BeanFactoryPostProcessor {
	
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
			BeanDefinitionBuilder definition = BeanDefinitionBuilder.rootBeanDefinition(CdiFactoryBean.class)
						.addPropertyValue("beanClass", bean.getBeanClass())
						.addPropertyValue("beanManager", beanManagerLocationUtil.beanManager())
						.setLazyInit(true);
			String name = generateName(bean);
			factory.registerBeanDefinition(name, definition.getBeanDefinition());
		}
	}

	private String generateName(Bean<?> bean) {
		Named named = (Named) bean.getBeanClass().getAnnotation(Named.class);
		String name = named != null ? named.value() : generateNameBasedOnClassName(bean);
		return name;
	}

	private String generateNameBasedOnClassName(Bean<?> bean) {
		return !useLongName ? bean.getBeanClass().getSimpleName() + "FactoryBean" : bean.getBeanClass().getName().replace(".", "_") + "FactoryBean";
	}

	
	
	public void setUseLongName(boolean useLongName) {
		this.useLongName = useLongName;
	}


}
