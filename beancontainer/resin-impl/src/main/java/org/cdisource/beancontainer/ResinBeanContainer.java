package org.cdisource.beancontainer;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;

public class ResinBeanContainer extends AbstractBeanContainer {

//	@Override
//	public void registerContext(Context context, boolean replace) {
//		if (replace) {
//			delegate.getCdiManager().replaceContext(context);
//		} else {
//			delegate.getCdiManager().addContext(context);
//		}
//	}
	
	public Context getContextByScope(Class<? extends Annotation> scopeType){
		return delegate.getCdiManager().getContext(scopeType);
	}

	
	com.caucho.resin.ResinBeanContainer delegate;
	
	@Override
	protected BeanManager locateBeanManager() {
		return delegate.getCdiManager();
	}

	@Override
	protected boolean isInitialized() {
		return delegate != null;
	}

	@Override
	protected void doStart() throws Exception {
		delegate = new com.caucho.resin.ResinBeanContainer();
		delegate.start();		
	}

	@Override
	protected void doStop() {
		delegate.close();		
	}

}
