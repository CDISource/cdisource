package org.cdisource.springintegration.springsupport;

import org.springframework.context.ApplicationContext;

/**
 * Simple interface that locates a Spring application context.
 *
 * @author rick
 *
 */
public interface ApplicationContextLocator {
	ApplicationContext locateApplicationContext();
}
