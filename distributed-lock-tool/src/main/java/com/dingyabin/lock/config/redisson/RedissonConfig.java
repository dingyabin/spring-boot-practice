package com.dingyabin.lock.config.redisson;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/10/16.
 * Time:20:25
 */
@Configuration
public class RedissonConfig {

    @Resource
    private RedissonSingleProperty redissonProperty;


    @Bean
    public RedissonClient redisClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(redissonProperty.getAddress());
        singleServerConfig.setConnectionPoolSize(redissonProperty.getConnectionPoolSize());
        singleServerConfig.setConnectionMinimumIdleSize(redissonProperty.getConnectionMinimumIdleSize());
        singleServerConfig.setRetryAttempts(redissonProperty.getRetryAttempts());
        singleServerConfig.setRetryInterval(redissonProperty.getRetryInterval());
        return Redisson.create(config);
    }


}
