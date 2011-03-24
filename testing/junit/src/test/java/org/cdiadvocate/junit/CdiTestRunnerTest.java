package org.cdiadvocate.junit;

import javax.inject.Inject;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CdiTestRunner.class)
public class CdiTestRunnerTest {

	@Inject
	private MessageBean messageBean;
	
	@Test
	public void testMessageBeanIsInjected() {
		Assert.assertNotNull(messageBean);
		//verify it is what we expect
		String result = messageBean.getMessage();
		Assert.assertEquals("MessageFromBean", result);
	}
	
	@Test
	public void testCalculatorIsInjectedToMessageBean() {
		Assert.assertNotNull(messageBean);
		String result = messageBean.getMathMessage(3, 7);		
		Assert.assertEquals("3+7=10", result);		
	}
}
