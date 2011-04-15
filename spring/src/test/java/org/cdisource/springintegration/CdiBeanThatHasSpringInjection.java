package org.cdisource.springintegration;

import javax.inject.Inject;

public class CdiBeanThatHasSpringInjection {
	@Inject @Spring(name="fooBar")
	FooSpringBean springBean;

	@Inject @Spring(name="fooBarnotActuallyThere", required=false)
	FooSpringBean notActuallyThere;

	@Inject @Spring(type=FooSpringBean.class)
	FooSpringBean injectByType;

	
	public void validate () {
		if (springBean==null) {
			throw new IllegalStateException("spring bean was null");
		}
		if (notActuallyThere!=null) {
			throw new IllegalStateException("notActuallyThere should be null");
		}
		if (injectByType==null) {
			throw new IllegalStateException("injectByType should be there");
		}

	}
}
