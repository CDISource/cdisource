package org.cdisource.beancontainer;

import java.lang.annotation.Annotation;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;

import com.caucho.resin.BeanContainerRequest;

public class ResinBeanContainer extends AbstractBeanContainer {
	
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
		String resinConf = System.getProperty("org.cdisource.beancontainer.ResinBeanContainer.resinConf");
		if (resinConf!=null) {
			delegate.addBeansXml(resinConf);
		}
		delegate.start();		
	}

	@Override
	protected void doStop() {
		delegate.close();		
	}
	
	ThreadLocal<BeanContainerRequest> requestScopeHolder = new ThreadLocal<BeanContainerRequest>();
	@Override
	public void startScope(Class<?> scope) {
		if (scope == RequestScoped.class) {
			BeanContainerRequest beginRequest = delegate.beginRequest();
			requestScopeHolder.set(beginRequest);
		}else {
			//...
		}
	}

	@Override
	public void stopScope(Class<?> scope) {
		if (scope == RequestScoped.class) {
			requestScopeHolder.set(null);
		} else {
			//...
		}
		
	}


}
