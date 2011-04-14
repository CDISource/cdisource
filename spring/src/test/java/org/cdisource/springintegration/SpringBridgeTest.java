package org.cdisource.springintegration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBridgeTest {
	
	@Test
	public void injectAutowiredAnnotation() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SpringBeanUsingAutoWired bean = context.getBean("springBeanAuto", SpringBeanUsingAutoWired.class);
		assertNotNull(bean);
		bean.validate();
	}
	
	@Test
	public void injectStandard() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SpringBeanUsingStandardInjection bean = (SpringBeanUsingStandardInjection) context.getBean("springBeanStandard");
		assertNotNull(bean);
		bean.validate();
	}

	@Test
	public void grabNamedBean() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Object bean = context.getBean("someNamedCdiBean");
		assertNotNull(bean);
		assertTrue(bean instanceof NamedCdiBean);
	}

}
