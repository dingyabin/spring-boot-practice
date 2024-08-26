package com.dingyabin.cache.config;

import com.dingyabin.cache.config.pro.CaffeineCacheProperties;
import com.dingyabin.cache.config.pro.CaffeineSpecProperties;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/8/22.
 * Time:23:21
 */
@Configuration
public class CaffeineSpringCacheConfig {

    @Resource
    private CaffeineCacheProperties cacheProperties;

    public static final String CUSTOM_CACHE_1H = "CUSTOM_CACHE_1H";


    @Bean
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        //公共默认配置
        cacheManager.setCaffeine(buildCaffeine(cacheProperties.getDefaultMaxSize(), cacheProperties.getDefaultDuration(), cacheProperties.getTimeUnit()));

        //从配置文件中获取特定的cacheName配置
        List<CaffeineSpecProperties> specs = cacheProperties.getSpecs();
        if (!CollectionUtils.isEmpty(specs)) {
            specs.forEach(spec -> cacheManager.registerCustomCache(spec.getCacheName(), buildCaffeine(spec.getMaxSize(), spec.getDuration(), spec.getTimeUnit()).build()));
        }

        //手动指定特定的cacheName配置
        cacheManager.registerCustomCache(CUSTOM_CACHE_1H, buildCaffeine(100, 10, TimeUnit.SECONDS).build());
        return cacheManager;
    }


    private Caffeine<Object, Object> buildCaffeine(int maximumSize, int duration, TimeUnit timeUnit) {
        return Caffeine.newBuilder().maximumSize(maximumSize).expireAfterWrite(duration, timeUnit);
    }
}
