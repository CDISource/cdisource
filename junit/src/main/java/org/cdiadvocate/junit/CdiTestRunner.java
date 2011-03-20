package org.cdiadvocate.junit;

import javax.inject.Inject;

import org.cdiadvocate.beancontainer.BeanContainer;
import org.cdiadvocate.beancontainer.BeanContainerManager;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

/**
 * 
 * CDI Test Runner class that lets you run tests on CDI beans directly while
 * providing injection, and interception (even production). Simply annotate your
 * test class using the {@link RunWith} annotation, and create your tests as
 * normal. Use {@link Inject} to inject components into your tests.
 * 
 * <pre>
 * 
 * &#064;RunWith(CdiTestRunner.class)
 * public class SimpleTest {
 * 
 * 	&#064;Inject
 * 	private Calculator calculator;
 * 
 * 	&#064;Test
 * 	public void shouldAddUp() {
 * 		int result = calculator.add(3, 4);
 * 		assertEqual(7, result);
 * 	}
 * }
 * 
 * </pre>
 * 
 * 
 * @author Andy Gibson
 * 
 */
public class CdiTestRunner extends BlockJUnit4ClassRunner {

	public CdiTestRunner(Class<?> klass) throws InitializationError {
		super(klass);
	}

	private static BeanContainer beanContainer;

	/**
	 * Lazy initializes a bean container that exists on the classpath. 
	 * 
	 * @return instance of the CDI Bean container
	 */
	public static BeanContainer getBeanContainer() {
		if (beanContainer == null) {
			beanContainer = BeanContainerManager.getInstance();
			beanContainer.start();
		}
		return beanContainer;
	}

	@Override
	protected Object createTest() throws Exception {
		Class<?> clazz = getTestClass().getJavaClass();
		Object result = getBeanContainer().getBeanByType(clazz);
		if (result == null) {
			result = super.createTest();
		}
		return result;
	}
}
