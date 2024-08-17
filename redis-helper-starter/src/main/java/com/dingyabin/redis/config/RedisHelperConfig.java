package com.dingyabin.redis.config;

import com.dingyabin.redis.helper.RedisHelper;
import com.dingyabin.redis.limiter.RedisLimiterHelper;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @author 丁亚宾
 * Date: 2024/6/19.
 * Time:19:33
 */
@Configuration(proxyBeanMethods = false)
public class RedisHelperConfig {


    @Bean
    @ConditionalOnProperty(name = "limiter.redis.enable", havingValue = "true", matchIfMissing = false)
    @ConditionalOnMissingBean(RedisLimiterHelper.class)
    public RedisLimiterHelper redisLimiterHelper() {
        return new RedisLimiterHelper();
    }




    @Bean
    @ConditionalOnBean(StringRedisTemplate.class)
    public RedisHelper redisHelper(StringRedisTemplate stringRedisTemplate) {
        return new RedisHelper(stringRedisTemplate);
    }

}
