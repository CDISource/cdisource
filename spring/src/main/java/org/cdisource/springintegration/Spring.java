package org.cdisource.springintegration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import javax.enterprise.util.Nonbinding;
import javax.inject.Qualifier;


@Qualifier @Retention(RUNTIME) @Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface Spring {
	Class<?> type() default Object.class;
	String name() default "";
	@Nonbinding boolean required() default true;
}