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

    MonitorReportType value();

}
