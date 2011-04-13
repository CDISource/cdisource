package org.cdisource.springintegration;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringBridgeTest {
	
	@Test
	public void testMe() {
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
		SpringBean bean = context.getBean("springBean", SpringBean.class);
		assertNotNull(bean);
		bean.validate();
	}

}
