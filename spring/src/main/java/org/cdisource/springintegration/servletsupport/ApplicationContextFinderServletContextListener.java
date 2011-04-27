package org.cdisource.springintegration.servletsupport;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
//import javax.servlet.annotation.WebListener;

import org.cdisource.springintegration.springsupport.ApplicationContextLocator;
import org.cdisource.springintegration.springsupport.ApplicationContextLocatorImpl;
import org.cdisource.springintegration.springsupport.ApplicationContextLocatorManager;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

//@WebListener
public class ApplicationContextFinderServletContextListener implements ServletContextListener {


	@Override
	public void contextInitialized(ServletContextEvent sce) {
		WebApplicationContext requiredWebApplicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(sce.getServletContext());
		ApplicationContextLocatorImpl.putContext(requiredWebApplicationContext);
		ApplicationContextLocator applicationContextLocator = ApplicationContextLocatorManager.getInstance();
		sce.getServletContext().setAttribute(ApplicationContextLocator.class.getName(), applicationContextLocator);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().setAttribute(ApplicationContextLocator.class.getName(), null);
	}

}
