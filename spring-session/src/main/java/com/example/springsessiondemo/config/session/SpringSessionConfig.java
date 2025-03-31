package com.example.springsessiondemo.config.session;

import com.example.springsessiondemo.intecepter.LoginCheckInterceptor;
import com.example.springsessiondemo.intecepter.PermitCheckInterceptor;
import com.example.springsessiondemo.intecepter.RoleCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.session.web.http.HeaderHttpSessionIdResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 丁亚宾
 * Date: 2024/6/13.
 * Time:16:34
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800, redisNamespace = "king:spring:session")
public class SpringSessionConfig implements WebMvcConfigurer {


    @Bean
    public HeaderHttpSessionIdResolver headerHttpSessionIdResolver() {
        return HeaderHttpSessionIdResolver.xAuthToken();
    }


    /**
     * @see RedisHttpSessionConfiguration
     * @return RedisSerializer
     */
    @Bean("springSessionDefaultRedisSerializer")
    public RedisSerializer springSessionDefaultRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LoginCheckInterceptor loginCheckInterceptor = new LoginCheckInterceptor();
        registry.addInterceptor(loginCheckInterceptor).addPathPatterns("/**").order(10);

        RoleCheckInterceptor roleCheckInterceptor = new RoleCheckInterceptor();
        registry.addInterceptor(roleCheckInterceptor).addPathPatterns("/**").order(20);


        PermitCheckInterceptor permitCheckInterceptor = new PermitCheckInterceptor();
        registry.addInterceptor(permitCheckInterceptor).addPathPatterns("/**").order(21);

    }
}
