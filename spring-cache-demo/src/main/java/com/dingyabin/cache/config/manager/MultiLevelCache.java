package com.dingyabin.cache.config.manager;

import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

/**
 * @author Administrator
 * Date: 2025/8/6.
 * Time:18:13
 */
public class MultiLevelCache implements Cache {

    /**
     * 名字
     */
    private final String name;

    /**
     * 本地缓存
     */
    private final Cache firstCache;

    /**
     * 分布式缓存
     */
    private final Cache secondCache;


    public MultiLevelCache(String name, Cache firstCache, Cache secondCache) {
        this.name = name;
        this.firstCache = firstCache;
        this.secondCache = secondCache;
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }


    @Override
    public ValueWrapper get(Object key) {
        //先查L1缓存
        ValueWrapper l1Value = firstCache.get(key);
        if (l1Value != null) {
            return l1Value;
        }
        //L1未命中，查L2缓存
        ValueWrapper l2Value = secondCache.get(key);
        if (l2Value != null) {
            //L2命中，回写到L1
            firstCache.put(key, l2Value.get());
            return l2Value;
        }
        return null;
    }



    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        if (wrapper == null) {
            return null;
        }
        Object value = wrapper.get();
        if (value != null && type != null && !type.isInstance(value)) {
            throw new IllegalStateException("Cached value is not of required type [" + type.getName() + "]: " + value);
        }
        return (T) value;
    }



    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }
        try {
            T value = valueLoader.call();
            put(key, value);
            return value;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void put(Object key, Object value) {
        // 同时写入L1和L2
        firstCache.put(key, value);
        secondCache.put(key, value);
    }


    @Override
    public void evict(Object key) {
        // 同时清除L1和L2
        firstCache.evict(key);
        secondCache.evict(key);
    }


    @Override
    public void clear() {
        firstCache.clear();
        secondCache.clear();
    }
}
