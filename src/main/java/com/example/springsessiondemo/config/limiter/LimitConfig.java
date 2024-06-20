package com.example.springsessiondemo.config.limiter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 丁亚宾
 * Date: 2024/6/19.
 * Time:19:33
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(name = "limiter.redis.enable", havingValue = "true", matchIfMissing = false)
public class LimitConfig {


    @Bean
    @ConditionalOnMissingBean(RedisLimiterHelper.class)
    public RedisLimiterHelper redisLimiterHelper() {
        return new RedisLimiterHelper();
    }


}
