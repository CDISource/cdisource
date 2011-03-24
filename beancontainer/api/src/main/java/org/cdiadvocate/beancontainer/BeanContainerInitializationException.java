package org.cdiadvocate.beancontainer;

/**
 * Exception class thrown when the {@link BeanContainer} fails to initialize
 * 
 * @author Andy Gibson
 * 
 */
public class BeanContainerInitializationException extends RuntimeException {

	private static final long serialVersionUID = -8787336526656793438L;

	public BeanContainerInitializationException(String message) {
		super(message);
	}

	public BeanContainerInitializationException(String message, Throwable cause) {
		super(message, cause);
	}
}
