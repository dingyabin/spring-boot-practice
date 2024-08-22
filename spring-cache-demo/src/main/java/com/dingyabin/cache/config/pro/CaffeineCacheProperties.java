package com.dingyabin.cache.config.pro;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/8/23.
 * Time:1:02
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cache.caffeine")
public class CaffeineCacheProperties {

    private int defaultMaxSize;

    private int defaultDuration;

    private TimeUnit timeUnit;

    private List<CaffeineSpecProperties> specs;

    public TimeUnit getTimeUnit() {
        if (this.timeUnit == null) {
            throw new IllegalArgumentException("CaffeineCacheProperties.timeUnit必须配置！");
        }
        return this.timeUnit;
    }
}
