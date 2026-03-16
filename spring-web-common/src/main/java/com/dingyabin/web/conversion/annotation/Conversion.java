package com.dingyabin.web.conversion.annotation;

import com.dingyabin.web.conversion.core.handler.ConversionSerializer;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.lang.annotation.*;

/**
 * 通用翻译注解
 *
 * @author Lion Li
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@JacksonAnnotationsInside
@JsonSerialize(using = ConversionSerializer.class)
public @interface Conversion {

    /**
     * 类型 (需与实现类的 {@link ConversionType} 注解type对应)
     * <p>
     * 默认取当前字段的值 如果设置了 @{@link Conversion#mapper()} 则取映射字段的值
     */
    String conversionType();

    /**
     * 映射字段 (如果不为空则取此字段的值)
     */
    String mapper() default "";

    /**
     * 其他条件
     */
    String extra() default "";

}
