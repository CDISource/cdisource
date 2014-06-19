package org.cdisource.springintegration;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

public class ProducerBean {

    @Produces @Named("mailHost")
    public String mailHost() {
        return "mail.example.com";
    }

    @Produces @Named("mailReceiver")
    public String mailReceiver() {
        return "joe.user@example.com";
    }
}
