package com.dingyabin.cache.service;

import com.dingyabin.cache.config.CaffeineCacheConfig;
import com.dingyabin.cache.helper.StudentCacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/8/22.
 * Time:23:46
 */
@Service
@CacheConfig(cacheManager = "caffeineCacheManager")
public class CacheService {

    @Resource
    private StudentCacheManager studentCacheManager;



    @Cacheable(cacheNames = CaffeineCacheConfig.CUSTOM_CACHE_1H, key = "#name")
    public String getObject1(String name) {
        System.out.println("new了一个String: " + name);
        return new String(name);
    }



    public String getObject2(String name) {
        return studentCacheManager.getObject(name);
    }

}
