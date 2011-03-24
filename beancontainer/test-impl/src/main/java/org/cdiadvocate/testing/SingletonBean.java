package org.cdiadvocate.testing;

import javax.inject.Singleton;
/**
 * Singleton bean for testing CDI
 * 
 * @author Andy Gibson
 *
 */
@Singleton
public class SingletonBean {

	private int counter;

	public int getCounter() {
		return counter;
	}

	public synchronized void incrementCount() {
		counter++;
	}
}
