package org.cdisource.springintegration;
import org.springframework.beans.factory.annotation.Autowired;

public class SpringBeanUsingAutoWired {
		@Autowired
		CdiBean bean;
		
		public void validate() {
			if (bean == null) {
				throw new IllegalStateException("CDI bean is null");
			}
		}
		
}
