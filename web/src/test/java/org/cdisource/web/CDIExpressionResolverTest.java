package org.cdisource.web;
import javax.el.CompositeELResolver;
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;

import org.cdisource.testing.InjectedBean;
import org.cdisource.testing.NamedBean;
import org.cdisource.testing.SimpleBean;
import org.junit.Test;
import org.junit.Before;
import static junit.framework.Assert.assertNotNull;
import de.odysseus.el.util.SimpleContext;
import de.odysseus.el.util.SimpleResolver;


/**
 * 
 * @author Rick Hightower
 *
 */
public class CDIExpressionResolverTest {
	
	ExpressionFactory expressionFactory;
	ELContext context;

	@Before
	public void setUp() {
		expressionFactory = ExpressionFactory.newInstance();
		context = context();
		
	}

	@Test
	public void testGetObjectByNameUsingEL() {
		
		ValueExpression e = expressionFactory.createValueExpression(context, "${simpleBean}", SimpleBean.class);
		SimpleBean namedBean = (SimpleBean) e.getValue(context);
		assertNotNull(namedBean);
	}

	@Test
	//Broken
	public void testGetPropertyOfObjectByUsingEL() {
		ExpressionFactory expressionFactory = ExpressionFactory.newInstance();
		assertNotNull(expressionFactory);
		ELContext context = context();
		
		ValueExpression e = expressionFactory.createValueExpression(context, "${simpleBean.injectedBean}", InjectedBean.class);
		InjectedBean bean = (InjectedBean) e.getValue(context);
		assertNotNull(bean);
	}

	@Test
	//Broken
	public void testGetObjectByComplexNameUsingEL() {
		
		ValueExpression e = expressionFactory.createValueExpression(context, "${org.long.Name}", NamedBean.class);
		NamedBean namedBean = (NamedBean) e.getValue(context);
		assertNotNull(namedBean);
	}

	
	private ELContext context() {
		SimpleContext context = new SimpleContext();
		CompositeELResolver resolver = new CompositeELResolver();
		resolver.add(new CDIExpressionResolver());
		resolver.add(new SimpleResolver());
		context.setELResolver(resolver);
		return context;
	}
}
