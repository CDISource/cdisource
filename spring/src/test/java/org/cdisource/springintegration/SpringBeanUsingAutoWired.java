package org.cdisource.springintegration;
import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

public class SpringBeanUsingAutoWired {
		@Autowired
		CdiBean2 bean;
		
		@Autowired
		ClassWithInjectionPoints foo;
		
		@Inject @Named("mailHost")
		String mailHost;
		
		@Inject @Named("mailReceiver")
		String mailReceiver;
		
		public void validate() {
			if (bean == null) {
				throw new IllegalStateException("CDI bean is null");
			}
			if (foo==null) {
				throw new IllegalStateException("you got no foo fool!");
				
			}
			
			if (foo.bean()==null) {
				throw new IllegalStateException("you got no foo bean and I pity you!");
				
			}

			if (mailHost == null) {
				throw new IllegalStateException("mailHost is null");
				
			}

			if (mailReceiver == null) {
				throw new IllegalStateException("mailReceiver is null");
				
			}
		}
		
}
