package com.dingyabin.web.config;

import com.dingyabin.web.filter.WebCommonFilter;
import com.dingyabin.web.property.WebConfigProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 丁亚宾
 * Date: 2024/10/21.
 * Time:21:37
 */
@Configuration
@EnableConfigurationProperties(WebConfigProperty.class)
public class WebAutoConfiguration {


    @Bean
    public FilterRegistrationBean<WebCommonFilter> webCommonFilterRegister(WebConfigProperty webConfigProperty) {
        FilterRegistrationBean<WebCommonFilter> registration = new FilterRegistrationBean<>();
        //注入过滤器
        registration.setFilter(new WebCommonFilter(webConfigProperty));
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("webCommonFilter");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }

}
