package org.cdisource.springintegration;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME) @Target({TYPE, METHOD, FIELD, PARAMETER})
public @interface SpringLookup {
	String value();
}