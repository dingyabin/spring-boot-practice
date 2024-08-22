package com.dingyabin.cache.service;

import com.dingyabin.cache.config.CaffeineCacheConfig;
import com.dingyabin.cache.model.Student;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @author 丁亚宾
 * Date: 2024/8/22.
 * Time:23:46
 */
@Service
@CacheConfig(cacheManager = "caffeineCacheManager")
public class CacheService {



    @Cacheable(cacheNames = CaffeineCacheConfig.CUSTOM_CACHE_1H, key = "#name")
    public Student getStudent(String name){
        System.out.println("new了一个student: "+ name);
        return new Student(name, 10);
    }


}
