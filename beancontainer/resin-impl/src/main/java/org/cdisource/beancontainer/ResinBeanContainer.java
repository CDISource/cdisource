package org.cdisource.beancontainer;

import java.lang.annotation.Annotation;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.spi.BeanManager;

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

}
