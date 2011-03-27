package org.cdisource.testing;

import javax.enterprise.inject.Produces;

@SomeOtherQualifier
public class InjectedBeanFactory {
    @Produces @SomeOtherQualifier InjectedBean createTransport() {
        return new InjectedBean();
    }
}
