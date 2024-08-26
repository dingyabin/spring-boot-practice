package com.dingyabin.cache.helper.api;

import com.dingyabin.redis.helper.RedisHelper;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 丁亚宾
 * Date: 2024/8/27.
 * Time:0:13
 */
public abstract class MultiCacheManager<K, V> extends BaseCaffeineCacheManager<K, V> {

    private final Class<V> genericType;


    private int timeOut;


    @Resource
    private RedisHelper redisHelper;


    public MultiCacheManager() {
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        genericType = (Class<V>) actualTypeArguments[1];
    }


    @Override
    protected V generateObject(K key) {
        return redisHelper.getCacheObject(key.toString(), genericType);
    }


    @Override
    public void delObject(K key) {
        super.delObject(key);
        redisHelper.deleteKey(key.toString());
    }


    @Override
    public void addObject(K k, V v) {
        super.addObject(k, v);
        redisHelper.setCacheObject(k.toString(), v, timeOut);
    }

}