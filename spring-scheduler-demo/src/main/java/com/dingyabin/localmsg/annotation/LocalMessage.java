package com.dingyabin.localmsg.annotation;


import java.lang.annotation.*;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface LocalMessage {

    String bizType();

    int maxRetryTime();

    boolean sync() default true;

}
