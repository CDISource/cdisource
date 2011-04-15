package org.cdisource.springintegration;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringBeanUsingAutoWired {
		@Autowired
		CdiBean2 bean;
		
		@Autowired
		ClassWithInjectionPoints foo;
		
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
		}
		
}
