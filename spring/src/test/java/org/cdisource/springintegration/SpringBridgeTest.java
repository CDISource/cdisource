package org.cdisource.springintegration;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.cdisource.springintegration.springsupport.ApplicationContextLocatorImpl;
import org.cdisource.testing.junit.CdiTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@RunWith(CdiTestRunner.class)
public class SpringBridgeTest {

	ApplicationContext context;
	
	@Test
	public void injectAutowiredAnnotation() {

		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContextLocatorImpl.putContext(context);

		SpringBeanUsingAutoWired bean = context.getBean("springBeanAuto", SpringBeanUsingAutoWired.class);
		assertNotNull(bean);
		bean.validate();
		
		ApplicationContextLocatorImpl.putContext(null);

	}
	
//	@Test
//	public void injectStandard() {
//		context = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ApplicationContextLocatorImpl.putContext(context);
//
//		SpringBeanUsingStandardInjection bean = (SpringBeanUsingStandardInjection) context.getBean("springBeanStandard");
//		assertNotNull(bean);
//		bean.validate();
//		
//		ApplicationContextLocatorImpl.putContext(null);
//
//	}

	@Test
	public void grabNamedBean() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		ApplicationContextLocatorImpl.putContext(context);

		
		Object bean = context.getBean("someNamedCdiBean");
		assertNotNull(bean);
		assertTrue(bean instanceof NamedCdiBean);
		
		ApplicationContextLocatorImpl.putContext(null);


	}

}
