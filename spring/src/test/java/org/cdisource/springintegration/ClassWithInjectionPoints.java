package org.cdisource.springintegration;

import javax.inject.Inject;

public class ClassWithInjectionPoints {
	@Inject CdiBean bean;
	
	public CdiBean bean() {return bean;}

}
