package com.dingyabin.satoken.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 丁亚宾
 * Date: 2024/8/7.
 * Time:0:31
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {


    // 注册 Sa-Token 拦截器，打开注解式鉴权功能
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，打开注解式鉴权功能
        //registry.addInterceptor(new SaInterceptor()).addPathPatterns("/**").order(10);
        registry.addInterceptor(new SaUserContextInterceptor()).addPathPatterns("/**").order(1);
    }


}
