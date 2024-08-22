package com.dingyabin.cache.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/8/22.
 * Time:23:21
 */
@Configuration
public class CaffeineCacheConfig {

    @Value("${cache.caffeine.defaultMaxSize}")
    private int defaultMaxSize;

    @Value("${cache.caffeine.defaultMinutes}")
    private int defaultMinutes;

    public static final String CUSTOM_CACHE_1H = "CUSTOM_CACHE_1H";
    public static final String CUSTOM_CACHE_2H = "CUSTOM_CACHE_2H";


    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        //公共默认配置
        cacheManager.setCaffeine(buildCaffeine(defaultMaxSize, defaultMinutes, TimeUnit.MINUTES));
        //特定的cacheName配置
        cacheManager.registerCustomCache(CUSTOM_CACHE_1H, buildCaffeine(100, 10, TimeUnit.SECONDS).build());
        cacheManager.registerCustomCache(CUSTOM_CACHE_2H, buildCaffeine(200, 20, TimeUnit.SECONDS).build());
        return cacheManager;
    }


    private Caffeine<Object, Object> buildCaffeine(int maximumSize, int duration, TimeUnit timeUnit) {
        return Caffeine.newBuilder().maximumSize(maximumSize).expireAfterWrite(duration, timeUnit);
    }
}
