package com.dingyabin.web.dynamic.anno;

import com.dingyabin.web.dynamic.DynamicPropertyResolverInterface;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicPropertyModel {

    String resolverBeanName() default "";

    Class<? extends DynamicPropertyResolverInterface> resolverBeanClass() default DynamicPropertyResolverInterface.class;

}
