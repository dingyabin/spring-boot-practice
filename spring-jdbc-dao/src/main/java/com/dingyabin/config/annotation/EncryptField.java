package com.dingyabin.config.annotation;

import java.lang.annotation.*;

/**
 * 字段加密注解
 *
 * @author 老马
 */
@Documented
@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EncryptField {



}
