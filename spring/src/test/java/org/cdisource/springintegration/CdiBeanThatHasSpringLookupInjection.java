package org.cdisource.springintegration;

import javax.inject.Inject;

public class CdiBeanThatHasSpringLookupInjection {
	@Inject @SpringLookup("fooBar2")
	FooSpringBean springBean;

	@Inject @SpringLookup("fooBar2")
	FooSpringBean springBean2;

	
	public void validate () {
		if (springBean==null) {
			throw new IllegalStateException("spring bean was null");
		}
		if (springBean2==null) {
			throw new IllegalStateException("spring bean2 was null");
		}

	}
}
