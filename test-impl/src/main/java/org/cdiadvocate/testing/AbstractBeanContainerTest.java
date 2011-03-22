package org.cdiadvocate.testing;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import org.cdiadvocate.beancontainer.BeanContainer;
import org.cdiadvocate.beancontainer.BeanContainerManager;
import org.cdiadvocate.beancontainer.BeanNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class AbstractBeanContainerTest {

	private BeanContainer beanContainer;

	public BeanContainer getBeanContainer() {
		return beanContainer;
	}

	@Before
	public void doBefore() {
		beanContainer = BeanContainerManager.getInstance();
		beanContainer.start();
	}

	@After
	public void doAfter() {
		beanContainer.stop();
	}

	@Test
	public void shouldInitContainer() {
		assertNotNull("Bean container is not initialized", beanContainer);
	}

	@Test
	public void shouldGetBeanInstance() {
		assertNotNull(beanContainer);
		SimpleBean bean = beanContainer.getBeanByType(SimpleBean.class);
		assertNotNull("Could not locate bean instance by type", bean);
		assertNotNull("Bean hasn't had InjectedBean injected into it",
				bean.getInjectedBean());
	}

	@Test
	public void shouldGetBeanByName() {
		assertNotNull(beanContainer);
		SimpleBean bean = (SimpleBean) beanContainer
				.getBeanByName("simpleBean");
		assertNotNull("Could not locate bean instance by name", bean);
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionOnSecondStartup() {
		beanContainer.start();
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionFetchByNameWithoutStartup() {
		BeanContainer second = BeanContainerManager.getInstance();
		second.getBeanByName("somename");
	}

	@Test(expected = IllegalStateException.class)
	public void shouldThrowExceptionOnSecondInstanceWithoutStartup() {
		BeanContainer second = BeanContainerManager.getInstance();
		second.getBeanByType(SimpleBean.class);
	}

	@Test(expected = BeanNotFoundException.class)
	public void shouldThrowExceptionOnMissignBeanByName() {
		beanContainer.getBeanByName("missing_bean_name");
	}

	@Test(expected = BeanNotFoundException.class)
	public void shouldThrowExceptionOnMissignBeanByType() {
		beanContainer.getBeanByType(String.class);
	}

	@Test
	public void shouldHaveSingletonItem() {
		SimpleBean bean = beanContainer.getBeanByType(SimpleBean.class);
		assertNotNull("Named bean not found",bean);
		SingletonBean singleton = beanContainer.getBeanByType(SingletonBean.class);
		assertEquals("Found non-equal instances of a singleton bean",bean.getSingletonBean(),singleton);
	}	
}
