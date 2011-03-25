package org.cdisource.testing;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerManager;
import org.cdisource.beancontainer.BeanNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract container test that can be used to perform initial tests on a
 * {@link BeanContainer} implementation. To use, just create a test case that
 * extends this class and make sure an implementation is also in the project so
 * it can load the {@link BeanContainer} instance.
 * 
 * @author Andy Gibson
 * @author Rick Hightower
 * 
 */
public class AbstractBeanContainerTest {

    private BeanContainer beanContainer;

    public BeanContainer getBeanContainer() {
        return beanContainer;
    }

    @Before
    public void doBefore() {
    	BeanContainerManager.initialize();
        beanContainer = BeanContainerManager.getInstance();
    }

    @After
    public void doAfter() {
        BeanContainerManager.shutdown();
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

    @Test
    public void shouldGetBeanByNameNoCast() {
        assertNotNull(beanContainer);
        SimpleBean bean = beanContainer
                .getBeanByName(SimpleBean.class, "simpleBean");
        assertNotNull("Could not locate bean instance by name", bean);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionOnSecondStartup() {
        beanContainer.start();
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
        assertNotNull("Named bean not found", bean);
        SingletonBean singleton = beanContainer
                .getBeanByType(SingletonBean.class);
        assertEquals("Found non-equal instances of a singleton bean",
                bean.getSingletonBean(), singleton);
    }
    
    @Test
    public void testSimpleNameSpaces() {
    	Object bean = beanContainer.getBeanNamespace().findObject("simpleBean");    	
    	assertNotNull(bean);
    	assertTrue("Cannot locate simpleBean ",beanContainer.getBeanNamespace().contains("simpleBean"));
    }
}
