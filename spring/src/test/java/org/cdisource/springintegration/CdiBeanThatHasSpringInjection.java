package org.cdisource.springintegration;

import javax.inject.Inject;

public class CdiBeanThatHasSpringInjection {
	@Inject @Spring(name="fooBar")
	FooSpringBean springBean;
	
	public void validate () {
		if (springBean==null) {
			throw new IllegalStateException("spring bean was null");
		}
	}
}
