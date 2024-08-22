package com.dingyabin.cache.config.pro;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/8/23.
 * Time:1:09
 */
@Getter
@Setter
public class CaffeineSpecProperties {

    private String cacheName;

    private int maxSize;

    private int duration;

    private TimeUnit timeUnit;

    public TimeUnit getTimeUnit() {
        if (this.timeUnit == null) {
            throw new IllegalArgumentException("CaffeineSpecProperties.timeUnit必须配置！");
        }
        return this.timeUnit;
    }
}
