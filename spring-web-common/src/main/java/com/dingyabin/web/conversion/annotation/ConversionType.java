package com.dingyabin.web.conversion.annotation;



import com.dingyabin.web.conversion.core.ConversionInterface;

import java.lang.annotation.*;

/**
 * 翻译类型注解 (标注到{@link ConversionInterface} 的实现类)
 *
 * @author Lion Li
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface ConversionType {

    /**
     * 类型
     */
    String type();

}
