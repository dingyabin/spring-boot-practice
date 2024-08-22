package com.dingyabin.cache.config.pro;

import lombok.Getter;
import lombok.Setter;

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

    private int minutes;
}
