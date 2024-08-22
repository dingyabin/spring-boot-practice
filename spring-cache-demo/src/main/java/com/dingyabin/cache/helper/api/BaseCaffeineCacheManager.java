package com.dingyabin.cache.helper.api;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import lombok.Getter;
import lombok.Setter;

/**
 * @author 丁亚宾
 * Date: 2024/8/23.
 * Time:0:05
 */
public abstract class BaseCaffeineCacheManager<K, V> {

    @Getter
    @Setter
    protected LoadingCache<K, V> loadingCache;


    public BaseCaffeineCacheManager() {
        Caffeine<Object, Object> caffeine = customizers(Caffeine.newBuilder());
        loadingCache = caffeine.build(this::generateObject);
    }


    public V getObject(K key){
        return loadingCache.get(key);
    }


    public void delObject(K key){
         loadingCache.invalidate(key);
    }


    protected abstract Caffeine<Object, Object> customizers(Caffeine<Object, Object> kvCaffeine);


    protected abstract V generateObject(K key);

}
