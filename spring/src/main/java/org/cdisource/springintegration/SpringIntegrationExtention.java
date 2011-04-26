package org.cdisource.springintegration;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.enterprise.util.AnnotationLiteral;
import javax.enterprise.context.Dependent;
import javax.inject.Named;

import org.cdisource.springintegration.springsupport.ApplicationContextLocatorManager;
import org.springframework.context.ApplicationContext;


public class SpringIntegrationExtention implements Extension {
	Map <String, SpringBean> springBeans = new HashMap<String, SpringBean>();
	
	
	public void processInjectionTarget (@Observes ProcessInjectionTarget<?> pit, BeanManager bm) {
		
		Set<InjectionPoint> injectionPoints = pit.getInjectionTarget().getInjectionPoints();
		
		synchronized (springBeans) {
			for (InjectionPoint point: injectionPoints){
				
				if (!(point.getType() instanceof Class<?>)) {
					continue;
				}
				
				Class<?> injectionType = (Class<?>) point.getType();
				Spring spring = point.getAnnotated().getAnnotation(Spring.class);
				if (spring!=null) {
					SpringBean springBean = new SpringBean(pit.getAnnotatedType(), spring, injectionType, bm);
					springBeans.put(springBean.key(), springBean); //we can do some validation to make sure that this bean is compatible with the one we are replacing.
				} else {
					SpringLookup springLookup = point.getAnnotated().getAnnotation(SpringLookup.class);
					if (springLookup!=null) {
						SpringBean springBean = new SpringBean(springLookup, injectionType, bm);
						springBeans.put(springBean.key(), springBean);
					}
				}
			}
		}
	}
	
	
	void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
	}
	
	
	void afterBeanDiscovery( @Observes AfterBeanDiscovery abd, BeanManager bm) {
		synchronized (springBeans) {
			for (SpringBean bean : springBeans.values()) {
				abd.addBean(bean);
			}	
		}
	}

	
	
	class SpringBean implements Bean <Object> {
		//InjectionTarget<Object> it;
		Spring spring;
		SpringLookup lookup;
		Class<?> injectionType; 
		BeanManager bm;
	

		AnnotatedType<?> annotatedType;
		
		SpringBean(AnnotatedType<?> annotatedType, Spring spring, Class<?> injectionType, BeanManager bm){
			this.spring = spring;
			this.injectionType = injectionType;
			this.bm = bm;
			this.annotatedType = annotatedType;
			//it = bm.createInjectionTarget(at);
		}
		
		public SpringBean(SpringLookup springLookup, Class<?> type,
				BeanManager bm) {
			this.lookup = springLookup;
			this.injectionType = type;
			this.bm = bm;
		}

		public String key () {
			return "" + this.getName() + "::" + injectionType.toString();
		}
		
		@SuppressWarnings("all")
		class NamedLiteral extends AnnotationLiteral<Named> implements Named {

			@Override
			public String value() {
				return (spring!=null) ? spring.name() : lookup.value();
			}
			
		}

		@Override
		public Class<?> getBeanClass() {
			return this.injectionType;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Set<InjectionPoint> getInjectionPoints() {
			return Collections.EMPTY_SET;
		}
		
		@Override
		public String getName() {
			return spring!=null ? spring.name() : lookup.value();
		}

		@Override
		public Set<Annotation> getQualifiers() {
			Set<Annotation> qualifiers = new HashSet<Annotation>();	
			if (lookup==null) {
				qualifiers.add(new NamedLiteral()); //Added this because it causes OWB to fail if there is a Named
			}
			if (spring!=null) {
				qualifiers.add( spring );
			} else {
				qualifiers.add( lookup );				
			}
			return qualifiers;
		}
		
		@Override
		public Class<? extends Annotation> getScope() {
			return Dependent.class;
		}

		@Override
		public Set<Class<? extends Annotation>> getStereotypes() {
			return Collections.emptySet();
		}

		@Override
		public Set<Type> getTypes() {
			Set<Type> types = new HashSet<Type>(); 
			types.add(this.injectionType); 
			types.add(Object.class); 
			return types;			
		}

		@Override
		public boolean isAlternative() {
			return false;
		}

		@Override
		public boolean isNullable() {
			return spring != null ? !spring.required() : false;
		}
		@Override
		public Object create(CreationalContext<Object> ctx) {
			ApplicationContext applicationContext = ApplicationContextLocatorManager.getInstance().locateApplicationContext();
			if  (applicationContext==null) {
				if (spring !=null) {
					System.err.printf("############## spring name=%s type=%s \n\n\n", spring.name(), spring.type());
				} else {
					System.err.printf("############## lookup value=%s \n\n\n", lookup.value());					
				}
				throw new IllegalStateException("applicationContext was null");
			}
			Object instance = null;
			if (spring!=null) {
			if (!spring.name().trim().equals("")) {
				if(!spring.required()) {
					if (applicationContext.containsBean(spring.name())) {
						instance = applicationContext.getBean(spring.name(), spring.type());						
					}
				} else {
					instance = applicationContext.getBean(spring.name(), spring.type());
				}
			} else {
				instance = applicationContext.getBean(spring.type());
			}
			} else {
				instance = applicationContext.getBean(lookup.value());				
			}
			return instance;
		}

		@Override
		public void destroy(Object instance, CreationalContext<Object> ctx) {
			ctx.release();
		}

		public String toString() {
			return String.format("SpringBean(hc=%d, hc=%d, annotatedType=%s, qualifiers=%s)", this.hashCode(), SpringIntegrationExtention.this.hashCode(), this.annotatedType, this.getQualifiers() );
		}
		
	}


}
