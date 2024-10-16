package com.dingyabin.lock.config.redisson;

import lombok.Getter;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 丁亚宾
 * Date: 2024/10/16.
 * Time:20:25
 */
@Getter
@Setter
@Configuration
public class RedissonConfig {

    @Value("${redisson.config.address}")
    private String address;

    @Value("${redisson.config.connectionPoolSize}")
    private int connectionPoolSize;

    @Value("${redisson.config.connectionMinimumIdleSize}")
    private int connectionMinimumIdleSize;

    @Value("${redisson.config.retryAttempts}")
    private int retryAttempts;

    @Value("${redisson.config.retryInterval}")
    private int retryInterval;


    @Bean
    public RedissonClient redisClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        singleServerConfig.setConnectionPoolSize(connectionPoolSize);
        singleServerConfig.setConnectionMinimumIdleSize(connectionMinimumIdleSize);
        singleServerConfig.setRetryAttempts(retryAttempts);
        singleServerConfig.setRetryInterval(retryInterval);
        return Redisson.create(config);
    }


}
