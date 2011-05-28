package org.cdisource.springintegration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
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
			System.out.println("bean types = " + bean.getTypes());
			Class<?> beanClass = getBeanClass(bean);
			BeanDefinitionBuilder definition = BeanDefinitionBuilder.rootBeanDefinition(CdiFactoryBean.class)
						.addPropertyValue("beanClass", beanClass)
						.addPropertyValue("beanManager", beanManagerLocationUtil.beanManager())
						.addPropertyValue("qualifiers", bean.getQualifiers())
						.setLazyInit(true);
			String name = generateName(bean);
			factory.registerBeanDefinition(name, definition.getBeanDefinition());
			System.out.println("bean name = " + name +", bean class = " + beanClass.getName());
		}
	}

	private List<Annotation> getAnnotation(Bean<?> bean) {
		Named named = bean.getBeanClass().getAnnotation(Named.class);
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		if (named != null) {
			annotations.add(named);
		}
		return annotations;
	}

	private Class<?> getBeanClass(Bean<?> bean) {
//        if (bean.getTypes().contains(bean.getBeanClass())) {
//            return bean.getBeanClass();
//        }
//        
//        if (bean.getTypes().contains(String.class)) {
//        	return String.class;
//        }
//        
//	    Stateless stateless = bean.getBeanClass().getAnnotation(Stateless.class);
//	    if (stateless == null) {
//	    	return bean.getBeanClass();
//	    }
//	    for (Type type : bean.getTypes()) {
//	    	if (type instanceof Class) {
//	    		Class<?> cls = (Class<?>) type;
//		    	if (cls.isInterface()) {
//		    		return cls;
//		    	}
//	    	}
//	    }
//		return null;
		return getMostDerivedClass(bean);
	}
	
	private Class<?> getMostDerivedClass(Bean<?> bean) {
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
