package org.cdisource.beancontainer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;

public class BeanContainerExtention implements Extension {
	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {

	}
}
