package com.dingyabin.cache.config.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Administrator
 * Date: 2025/8/6.
 * Time:18:06
 */
public class MultiLevelCacheManager implements CacheManager {

    /**
     * 本地缓存
     */
    private final CacheManager firstCacheManager;

    /**
     * 分布式缓存
     */
    private final CacheManager secondCacheManager;



    public MultiLevelCacheManager(CacheManager firstCacheManager, CacheManager secondCacheManager) {
        this.firstCacheManager = firstCacheManager;
        this.secondCacheManager = secondCacheManager;
    }



    @Override
    public Cache getCache(String name) {
        return new MultiLevelCache(name, firstCacheManager.getCache(name), secondCacheManager.getCache(name));
    }



    @Override
    public Collection<String> getCacheNames() {
        Set<String> names = new HashSet<>();
        names.addAll(firstCacheManager.getCacheNames());
        names.addAll(secondCacheManager.getCacheNames());
        return names;
    }


}
