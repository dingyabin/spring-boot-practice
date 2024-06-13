package com.example.springsessiondemo.annotation;

import java.lang.annotation.*;

/**
 * @author 丁亚宾
 * Date: 2024/6/13.
 * Time:20:41
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NoNeedLogin {
}
