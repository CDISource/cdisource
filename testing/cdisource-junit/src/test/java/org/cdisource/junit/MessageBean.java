package org.cdisource.junit;

import javax.inject.Inject;

public class MessageBean {

	@Inject
	private Calculator calculator;
	
	public String getMessage() {
		return "MessageFromBean";
	}
	
	public String getMathMessage(int num1,int num2) {
		return num1+"+"+num2+"="+calculator.add(num1,num2);
	}
}
