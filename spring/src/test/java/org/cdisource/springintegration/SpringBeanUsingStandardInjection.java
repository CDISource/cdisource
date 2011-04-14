package org.cdisource.springintegration;

public class SpringBeanUsingStandardInjection {
		
		NamedCdiBean bean;
		
		public void setBean(NamedCdiBean bean) {
			this.bean = bean;
		}

		public void validate() {
			if (bean == null) {
				throw new IllegalStateException("CDI bean is null");
			}
		}
		
}
