package org.cdisource.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.cdisource.beancontainer.BeanContainer;
import org.cdisource.beancontainer.BeanContainerManager;

@WebListener
public class CDIServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		sce.getServletContext().setAttribute(BeanContainer.class.getName(), BeanContainerManager.getInstance());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().setAttribute(BeanContainer.class.getName(), null);
	}

}
