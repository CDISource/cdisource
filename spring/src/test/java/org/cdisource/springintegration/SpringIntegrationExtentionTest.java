package org.cdisource.springintegration;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.cdisource.springintegration.springsupport.ApplicationContextLocatorManager;
import org.cdisource.springintegration.springsupport.ApplicationContextLocatorImpl;
import org.cdisource.testing.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.cdisource.beancontainer.BeanContainer;



@RunWith(CdiTestRunner.class)
public class SpringIntegrationExtentionTest {
	
	@Inject
	BeanContainer beanContainer;
	
	ApplicationContext context;
	
	@After
	public void tearDown() {
		ApplicationContextLocatorImpl.putContext(null);
	}
	
	@Test
	public void testSpringQualifier() {
		context = new ClassPathXmlApplicationContext("springIntoCdiApplicationContext.xml");
		ApplicationContextLocatorImpl.putContext(context);

		assertNotNull(beanContainer);
		assertNotNull(ApplicationContextLocatorManager.getInstance().locateApplicationContext());
		CdiBeanThatHasSpringInjection springInjection = beanContainer.getBeanByType(CdiBeanThatHasSpringInjection.class);
		assertNotNull(springInjection);
		springInjection.validate();
	}
	

}
