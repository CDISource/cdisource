package org.cdiadvocate.testing;

import javax.inject.Singleton;

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
