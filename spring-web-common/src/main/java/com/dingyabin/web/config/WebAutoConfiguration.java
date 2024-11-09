package com.dingyabin.web.config;

import com.dingyabin.web.config.strategy.TraceIdStyleStrategy;
import com.dingyabin.web.filter.WebCommonFilter;
import com.dingyabin.web.property.WebConfigProperty;
import com.dingyabin.web.security.HttpServletRequestBodyProcessor;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 丁亚宾
 * Date: 2024/10/21.
 * Time:21:37
 */
@Configuration
@EnableConfigurationProperties(WebConfigProperty.class)
public class WebAutoConfiguration {


    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String datePattern;


    @Bean
    public FilterRegistrationBean<WebCommonFilter> webCommonFilterRegister(WebConfigProperty webConfigProperty,
                                                                           @Autowired(required = false) HttpServletRequestBodyProcessor bodyProcessor,
                                                                           @Autowired(required = false) TraceIdStyleStrategy traceIdStyleStrategy) {
        FilterRegistrationBean<WebCommonFilter> registration = new FilterRegistrationBean<>();
        //注入过滤器
        registration.setFilter(new WebCommonFilter(webConfigProperty, bodyProcessor, traceIdStyleStrategy));
        //拦截规则
        registration.addUrlPatterns("/*");
        //过滤器名称
        registration.setName("webCommonFilter");
        //过滤器顺序
        registration.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return registration;
    }


    @Bean
    @ConditionalOnProperty(prefix = "web.common.config", name = "enableJavaTimeModule", havingValue = "true", matchIfMissing = true)
    public JavaTimeModule javaTimeModule() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(formatter));
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(formatter));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(formatter));

        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(formatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(formatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(formatter));
        return javaTimeModule;
    }

}
