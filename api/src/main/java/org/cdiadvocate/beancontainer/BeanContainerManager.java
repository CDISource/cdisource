package org.cdiadvocate.beancontainer;

import java.util.Properties;
import java.util.ServiceLoader;

public class BeanContainerManager {
    public static String PROP_NAME = "org.cdi.advocacy.BeanContainer";

    public static BeanContainer getInstance() {
        return getInstance(System.getProperties());
    }

    public static BeanContainer getInstance(Properties properties) {
        try {
            /* The property should override the ServiceLoader if found. */
            String beanContainerClassName = properties.getProperty(PROP_NAME);

            /* If the property was not found, use the service loader. */
            if (beanContainerClassName == null) {
                ServiceLoader<BeanContainer> instance = ServiceLoader
                        .load(BeanContainer.class);
                if (instance.iterator().hasNext()) {
                    return instance.iterator().next();
                }
            }

            /*
             * If class property not found in the passed properties, then Resin
             * is the default for now, we may switch to the RI.
             */
            beanContainerClassName = beanContainerClassName != null ? beanContainerClassName
                    : "org.cdiadvocate.beancontainer.WeldBeanContainer";

            /*
             * Get the classloader associated with the current webapp and not
             * the global classloader
             */
            ClassLoader contextClassLoader = Thread.currentThread()
                    .getContextClassLoader();
            Class<?> clazz = Class.forName(beanContainerClassName, true,
                    contextClassLoader);
            BeanContainer beanContainer = (BeanContainer) clazz.newInstance();
            return beanContainer;

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
