package com.dingyabin.prometheusdemo.intercepter;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:16:11
 */
@Component
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private PerformanceMonitorInterceptor performanceMonitorInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(performanceMonitorInterceptor).addPathPatterns("/**");
    }
}
