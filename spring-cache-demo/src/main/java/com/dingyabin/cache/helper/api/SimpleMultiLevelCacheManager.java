package com.dingyabin.cache.helper.api;

import com.dingyabin.redis.helper.RedisHelper;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Resource;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author 丁亚宾
 * Date: 2024/8/27.
 * Time:0:13
 */
public abstract class SimpleMultiLevelCacheManager<K, V> extends BaseCaffeineCacheManager<K, V> {

    private final Class<V> genericType;

    @Getter
    @Setter
    protected int timeOut = 60;

    /**
     * 是否允许从持久层加载数据
     */
    protected boolean allowLoadObjectInRepository = true;


    @Resource
    private RedisHelper redisHelper;


    public SimpleMultiLevelCacheManager() {
        init();
        ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        genericType = (Class<V>) actualTypeArguments[1];
    }


    @Override
    protected V generateObject(K key) {
        V cacheObject = redisHelper.getCacheObject(key.toString(), genericType);
        if (cacheObject == null && allowLoadObjectInRepository && (cacheObject = loadObjectInRepository(key)) != null) {
            redisHelper.setCacheObject(key.toString(), cacheObject, timeOut);
        }
        return cacheObject;
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


    /**
     * 执行一些初始化任务，比如设置过期时间
     */
    protected abstract void init();


    /**
     * 从数据库查找
     *
     * @param key key
     * @return V
     */
    protected abstract V loadObjectInRepository(K key);

}