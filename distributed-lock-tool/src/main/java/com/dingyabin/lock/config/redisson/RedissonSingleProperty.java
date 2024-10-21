package com.dingyabin.lock.config.redisson;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 丁亚宾
 * Date: 2024/10/21.
 * Time:20:19
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "redisson.config")
public class RedissonSingleProperty {

    private String address;

    private int connectionPoolSize;

    private int connectionMinimumIdleSize;

    private int retryAttempts;

    private int retryInterval;


}
