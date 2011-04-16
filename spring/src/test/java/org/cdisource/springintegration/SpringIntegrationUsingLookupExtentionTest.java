package org.cdisource.springintegration;

import static org.junit.Assert.assertNotNull;

import javax.inject.Inject;

import org.cdisource.springintegration.springsupport.ApplicationContextLocatorThreadLocal;
import org.cdisource.testing.junit.CdiTestRunner;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.cdisource.beancontainer.BeanContainer;



@RunWith(CdiTestRunner.class)
public class SpringIntegrationUsingLookupExtentionTest {
	
	@Inject
	BeanContainer beanContainer;
	
	ApplicationContext context;
	
	@Before
	public void setUp() {
		context = new ClassPathXmlApplicationContext("springIntoCdiApplicationContext.xml");
		ApplicationContextLocatorThreadLocal.putContext(context);
	}

	@After
	public void tearDown() {
		ApplicationContextLocatorThreadLocal.putContext(null);
	}

	@Test
	public void testSpringLookupQualifier() {
		assertNotNull(beanContainer);
		CdiBeanThatHasSpringLookupInjection springInjection = beanContainer.getBeanByType(CdiBeanThatHasSpringLookupInjection.class);
		assertNotNull(springInjection);
		springInjection.validate();
	}
		
}
