package org.cdisource.springintegration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBridgeTest {

	ApplicationContext context;
	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	
	@Test
	public void injectAutowiredAnnotation() {
		SpringBeanUsingAutoWired bean = context.getBean("springBeanAuto", SpringBeanUsingAutoWired.class);
		assertNotNull(bean);
		bean.validate();
	}
	
	@Test
	public void injectStandard() {
		SpringBeanUsingStandardInjection bean = (SpringBeanUsingStandardInjection) context.getBean("springBeanStandard");
		assertNotNull(bean);
		bean.validate();
	}

	@Test
	public void grabNamedBean() {
		Object bean = context.getBean("someNamedCdiBean");
		assertNotNull(bean);
		assertTrue(bean instanceof NamedCdiBean);
	}

}
