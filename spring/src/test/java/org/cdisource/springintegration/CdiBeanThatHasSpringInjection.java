package org.cdisource.springintegration;

import javax.inject.Inject;

public class CdiBeanThatHasSpringInjection {
	@Inject @Spring(name="fooBar") //Works with Weld and Candi but not OWB
	FooSpringBean springBean;
	@Inject @Spring(name="fooBarnotActuallyThere", required=false) //Works with Weld and Candi but not OWB
	FooSpringBean notActuallyThere;
	@Inject @Spring(type=FooSpringBean2.class) //Does not work in Weld but does work with CANDI
	FooSpringBean2 injectByType;

	
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
