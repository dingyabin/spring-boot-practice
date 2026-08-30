package com.dingyabin.web.dynamic.anno;

import com.dingyabin.web.dynamic.DynamicPropertyResolverInterface;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface DynamicPropertyModel {

    String resolverBeanName() default "";

    Class<? extends DynamicPropertyResolverInterface> resolverBeanClass() default DynamicPropertyResolverInterface.class;

    boolean resolverCanNull() default true;

    PropertyStrategy propertyStrategy() default PropertyStrategy.WITH_ANNOTATION;


    enum PropertyStrategy {
        /**
         * 所有字段
         */
        All() {
            @Override
            public boolean shouldDynamic(BeanPropertyWriter writer) {
                return true;
            }
        },
        /**
         *  标有注解的字段
         */
        WITH_ANNOTATION() {
            @Override
            public boolean shouldDynamic(BeanPropertyWriter writer) {
                return writer.getAnnotation(DynamicProperty.class) != null;
            }
        },

        /**
         * 除了有DynamicIgnoreProperty注解以外的所有字段
         */
        EXPECT_IGNORE_ANNOTATION() {
            @Override
            public boolean shouldDynamic(BeanPropertyWriter writer) {
                return writer.getAnnotation(DynamicIgnoreProperty.class) == null;
            }
        };

        public abstract boolean shouldDynamic(BeanPropertyWriter writer);
    }
}
