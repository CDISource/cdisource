package org.cdisource.web;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Named;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerManager;
import org.cdisource.beancontainer.namespace.BeanNamespace;

/**
 * JSF El Resolver that extends the {@link ELResolver} class to provide bean
 * lookups using EL expressions based on the CDI {@link Named} annotation.
 * <p/>
 * This class requires an instance of a {@link BeanContainer} implementation on
 * the class path to provide CDI services.
 * 
 * 
 * @author Andy Gibson
 * 
 */
public class CDIExpressionResolver extends ELResolver {

	@Override
	public Object getValue(ELContext context, Object base, Object property) {

		if (context == null) {
			throw new NullPointerException("EL Context is null");
		}
		
		if (property == null) {
			return null;
		}
		
		Object result = null;
		BeanContainer container = BeanContainerManager.getInstance();
		String stringProperty = property.toString();

		if (base == null) {
			// locate the property in the root namespace
			result = container.getBeanNamespace().findObject(stringProperty);
		} else {
			// check if this is a namespace object
			if (base instanceof BeanNamespace) {
				BeanNamespace namespace = (BeanNamespace) base;
				result = namespace.findObject(stringProperty);
				// check to see if our new object is the end of the line.
			}
		}

		// if we found a bean, extract it
		if (result instanceof Bean) {
			Bean<?> bean = (Bean<?>) result;
			BeanManager bm = container.getBeanManager();
			CreationalContext<?> creationalContext = bm
					.createCreationalContext(bean);

			result = bm.getReference(bean, bean.getBeanClass(),
					creationalContext);
		}

		context.setPropertyResolved(result != null);
		return result;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		return null;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property,
			Object value) {

	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return false;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context,
			Object base) {
		return null;
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return null;
	}

}
