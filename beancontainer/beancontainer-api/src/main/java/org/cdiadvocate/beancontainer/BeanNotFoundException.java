package org.cdiadvocate.beancontainer;


/**
 * Exception thrown when a bean cannot be located 
 * 
 * @author Andy Gibson
 *
 */
public class BeanNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -1122296335536887240L;

	public BeanNotFoundException(final String message) {
		super(message);
	}

	public BeanNotFoundException(final String message, Throwable cause) {
		super(message, cause);
	}
}
