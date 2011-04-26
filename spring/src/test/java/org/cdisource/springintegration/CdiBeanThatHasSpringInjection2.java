package org.cdisource.springintegration;

import javax.inject.Inject;

public class CdiBeanThatHasSpringInjection2 {
	@Inject @Spring(name="fooBar7") //Works with Weld and Candi but not OWB
	FooSpringBean springBean;

	
	public void validate () {
		if (springBean==null) {
			throw new IllegalStateException("spring bean was null");
		}
	}
}
