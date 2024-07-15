package com.dingyabin.prometheusdemo.aop;

import com.dingyabin.prometheusdemo.aop.enums.MonitorReportType;

import java.lang.annotation.*;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:22:16
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface MonitorReport {

    String name();

    MonitorReportType[] value();

    /**
     * 如果value选择了MonitorReportType.COUNTER_WITH_RES
     * 那么需要指定一个key，用于校验注解方法的返回结果是否正常
     * 如果不写，则会使用name()属性
     * @see ReturnVerifyHelper
     *
     * @return key
     */
    String resVerifyKey() default "";

}
