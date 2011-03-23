package org.cdiadvocate.beancontainer;

import java.util.Properties;
import java.util.ServiceLoader;

/**
 * Manager that provides access to the CDI implementation on the classpath by
 * returning an instance of the {@link BeanContainer} that represents that
 * implementation.
 * <p>
 * You can swap out the BeanContainer just by including the right jar file implementation in 
 * your project.
 * </p>
 * <p>
 * We look for a file under classpath/META-INF/services called org.cdiadvocate.beancontainer.BeanContainer.
 * </p>
 * This file has one of three values:
 * <ol>
 * <li>org.cdiadvocate.beancontainer.ResinBeanContainer</li>
 * <li>org.cdiadvocate.beancontainer.OpenWebBeansBeanContainer</li>
 * <li>org.cdiadvocate.beancontainer.WeldBeanContainer</li>
 * </ol>
 * 
 * 
 * <p>
 * You can also override which container is used using a System property
 * 
 * </p>
 * 
 * <code>$ java -Dorg.cdi.advocacy.BeanContainer=org.cdiadvocate.beancontainer.ResinBeanContainer</code>
 * 
 * <p></p>
 * 
 * @see java.util.ServiceLoader.
 * 
 * @author Rick Hightower
 * 
 */
public class BeanContainerManager {
    /** Property name that we use to look up the bean container override. */
    public static String PROP_NAME = "org.cdi.advocacy.BeanContainer";

    /** Get the bean container instance using system properties. 
     * @see BeanContainerManager.PROP_NAME
     * */
    public static BeanContainer getInstance() {
        return getInstance(System.getProperties());
    }

    /** Get the bean container passing in your own properties. */
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
