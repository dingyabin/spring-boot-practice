package com.dingyabin.redis.helper;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


/**
 * @author 丁亚宾
 * Date: 2023/11/28.
 * Time:22:53
 */
@Slf4j
public class RedisHelper extends AbstractRedisHelper {

    private final ValueOperations<String, String> stringOperations;

    private final ListOperations<String, String> listOperations;

    private final SetOperations<String, String> setOperations;

    private final ZSetOperations<String, String> zSetOperations;


    public RedisHelper(StringRedisTemplate stringRedisTemplate) {
        super(stringRedisTemplate);
        stringOperations = redisTemplate.opsForValue();
        listOperations = redisTemplate.opsForList();
        setOperations = redisTemplate.opsForSet();
        zSetOperations = redisTemplate.opsForZSet();
    }


    public void setCacheObject(String key, Object value, Integer timeout) {
        this.setCacheObject(key, value, timeout, TimeUnit.SECONDS);
    }


    public void setCacheObject(String key, Object value, Integer timeout, TimeUnit timeUnit) {
        stringOperations.set(key, toJson(value), (long) timeout, timeUnit);
    }


    public Long incr(String key) {
        return incrWithExpire(key, -1);
    }


    public Long incr(String key, Long delta) {
        return incrWithExpire(key, delta, -1, null);
    }


    public Long incrWithExpire(String key, Integer timeout) {
        return incrWithExpire(key, 1L, timeout, TimeUnit.SECONDS);
    }


    public Long incrWithExpire(String key, long delta, Integer timeout, TimeUnit timeUnit) {
        Long increment = stringOperations.increment(key, delta);
        if (timeout != null && timeout > 0) {
            expire(key, (long) timeout, timeUnit);
        }
        return increment;
    }


    public Long decr(String key, Integer timeout) {
        return decr(key, 1L, timeout, TimeUnit.SECONDS);
    }


    public Long decr(String key, long delta, Integer timeout, TimeUnit timeUnit) {
        Long decrement = stringOperations.decrement(key, delta);
        this.expire(key, (long) timeout, timeUnit);
        return decrement;
    }


    public <T> T getCacheObject(String key, Class<T> clazz) {
        String value = stringOperations.get(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return toJavaObject(value, clazz);
    }


    public String getCacheString(String key) {
        return stringOperations.get(key);
    }


    public List<String> multiGetString(List<String> keys) {
        return stringOperations.multiGet(keys);
    }


    public <T> List<T> multiGetObject(List<String> keys, Class<T> clazz) {
        List<String> strings = stringOperations.multiGet(keys);
        if (CollectionUtils.isEmpty(strings)) {
            return Collections.emptyList();
        }
        return toJavaList(strings, clazz);
    }


    /*********************************************List******************************************************/

    public void trim(String key, long start, long end) {
        listOperations.trim(key, start, end);
    }


    public void setList(String key, long index, Object value) {
        listOperations.set(key, index, toJson(value));
    }


    public void leftPush(String key, Object value) {
        listOperations.leftPush(key, toJson(value));
    }


    public void leftPush(String key, Object pivot, Object value) {
        listOperations.leftPush(key, toJson(pivot), toJson(value));
    }


    public void leftPushIfPresent(String key, Object value) {
        listOperations.leftPushIfPresent(key, toJson(value));
    }

    public void rightPush(String key, Object value) {
        listOperations.rightPush(key, toJson(value));
    }

    public void rightPush(String key, Object pivot, Object value) {
        listOperations.rightPush(key, toJson(pivot), toJson(value));
    }

    public void rightPushIfPresent(String key, Object value) {
        listOperations.rightPushIfPresent(key, toJson(value));
    }

    public void removeList(String key, long count, Object value) {
        listOperations.remove(key, count, toJson(value));
    }

    public <T> void leftPushAll(String key, List<T> dataList, Integer timeout) {
        this.leftPushAll(key, dataList, timeout, TimeUnit.SECONDS);
    }


    public <T> void leftPushAll(String key, List<T> dataList, Integer timeout, TimeUnit timeUnit) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (dataList.size() > 8000) {
            log.info("Redis集合类型leftPushAll集合对象大于8000，缓存Key:{}", key);
        }
        executePipelined(operations -> {
            operations.opsForList().leftPushAll(key, toListString(dataList));
            operations.expire(key, (long) timeout, timeUnit);
        });
    }


    public <T> void rightPushAll(String key, List<T> dataList, Integer timeout) {
        this.rightPushAll(key, dataList, timeout, TimeUnit.SECONDS);
    }


    public <T> void rightPushAll(String key, List<T> dataList, Integer timeout, TimeUnit timeUnit) {
        if (CollectionUtils.isEmpty(dataList)) {
            return;
        }
        if (dataList.size() > 8000) {
            log.info("Redis集合类型rightPushAll集合对象大于8000，缓存Key:{}", key);
        }

        executePipelined(operations -> {
            operations.opsForList().rightPushAll(key, toListString(dataList));
            operations.expire(key, (long) timeout, timeUnit);
        });
    }


    public <T> List<T> listObjectRange(String key, Class<T> clazz) {
        return this.listObjectRange(key, clazz, 0L, -1L);
    }


    public <T> List<T> listObjectRange(String key, Class<T> clazz, long start, long end) {
        List<String> range = listOperations.range(key, start, end);
        return toJavaList(range, clazz);
    }


    public List<String> listStrRange(String key) {
        return this.listStrRange(key, 0L, -1L);
    }


    public List<String> listStrRange(String key, long start, long end) {
        return listOperations.range(key, start, end);
    }


    public Long listSize(String key) {
        return listOperations.size(key);
    }


    public <T> T index(String key, Class<T> clazz, long index) {
        String object = listOperations.index(key, index);
        return (null == object) ? null : toJavaObject(object, clazz);
    }


    public String index(String key, long index) {
        return listOperations.index(key, index);
    }


    public <T> T leftPop(String key, Class<T> clazz) {
        String object = listOperations.leftPop(key);
        return (null == object) ? null : this.toJavaObject(object, clazz);
    }


    public String leftPop(String key) {
        return listOperations.leftPop(key);
    }


    public <T> T rightPop(String key, Class<T> clazz) {
        String object = listOperations.rightPop(key);
        return (null == object) ? null : this.toJavaObject(object, clazz);
    }


    public String rightPop(String key) {
        return listOperations.rightPop(key);
    }


    /*****************************HASH********************************************/


    public Long deleteHash(String key, Object... hashKeys) {
        return redisTemplate.opsForHash().delete(key, hashKeys);
    }


    public Boolean hasHashKey(String key, String hashKey) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.hasKey(key, hashKey);
    }


    public <T> T getHashKey(String key, Class<T> clazz, String hashKey) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        String object = hashOperations.get(key, hashKey);
        return (null == object) ? null : this.toJavaObject(object, clazz);
    }


    public <T> List<T> multiGetHashKeys(String key, Class<T> clazz, List<String> hashKeys) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        List<String> objects = hashOperations.multiGet(key, hashKeys);
        return this.toJavaList(objects, clazz);
    }


    public Long incrHashKey(String key, String hashKey, long delta) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.increment(key, hashKey, delta);
    }


    public Double incrHashKey(String key, String hashKey, double delta) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.increment(key, hashKey, delta);
    }

    public Set<String> hashKeys(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.keys(key);
    }


    public Long hashSize(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.size(key);
    }


    public void putAll(String key, Map<String, String> dataMap, Integer timeout) {
        this.putAll(key, dataMap, timeout, TimeUnit.SECONDS);
    }


    public void putAll(String key, Map<String, String> dataMap, Integer timeout, TimeUnit timeUnit) {
        if (CollectionUtils.isEmpty(dataMap)) {
            return;
        }
        executePipelined(operations -> {
            operations.opsForHash().putAll(key, dataMap);
            operations.expire(key, (long) timeout, timeUnit);
        });
    }


    public void putHashKey(String key, String hashKey, String value, Integer timeout) {
        this.putHashKey(key, hashKey, value, timeout, TimeUnit.SECONDS);
    }


    public void putHashKey(String key, String hashKey, String value, Integer timeout, TimeUnit timeUnit) {
        executePipelined(operations -> {
            operations.opsForHash().put(key, hashKey, value);
            operations.expire(key, (long) timeout, timeUnit);
        });
    }

    public void putHashKey(String key, String hashKey, String value) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        hashOperations.put(key, hashKey, value);
    }


    public Map<String, String> getCacheMap(String key) {
        HashOperations<String, String, String> hashOperations = this.redisTemplate.opsForHash();
        return hashOperations.entries(key);
    }


    /********************************************SET**********************************************/


    public void addAll(String key, Set<String> dataSet, Integer timeout) {
        this.addAll(key, dataSet, timeout, TimeUnit.SECONDS);
    }


    public void addAll(String key, Set<String> dataSet, Integer timeout, TimeUnit timeUnit) {
        executePipelined(operations -> {
            operations.opsForSet().add(key, dataSet.toArray());
            operations.expire(key, (long) timeout, timeUnit);
        });
    }


    public Set<String> getCacheSet(String key) {
        return setOperations.members(key);
    }


    public Long setRemove(String key, Object... values) {
        return setOperations.remove(key, values);
    }


    public String pop(String key) {
        return setOperations.pop(key);
    }


    public List<String> pop(String key, long count) {
        return setOperations.pop(key, count);
    }


    public Long setSize(String key) {
        return setOperations.size(key);
    }

    public Set<String> randomMembers(String key, long count) {
        return setOperations.distinctRandomMembers(key, count);
    }


    public Boolean isMember(String key, String value) {
        return setOperations.isMember(key, value);
    }

    /*****************************************ZSET****************************************************/


    public void addZset(String key, String value, double score) {
        zSetOperations.add(key, value, score);
    }


    public Double incrScore(String key, String value, double delta) {
        return zSetOperations.incrementScore(key, value, delta);
    }

    public Long zSetRemove(String key, Object... value) {
        return zSetOperations.remove(key, value);
    }

    public Long rank(String key, Object value) {
        return zSetOperations.rank(key, value);
    }

    public Long reverseRank(String key, Object value) {
        return zSetOperations.reverseRank(key, value);
    }

    public Set<String> zSetRange(String key) {
        return this.zSetRange(key, 0L, -1L);
    }

    public Set<String> zSetRange(String key, long start, long end) {
        return zSetOperations.range(key, start, end);
    }

    public Set<String> reverseRange(String key, long start, long end) {
        return zSetOperations.reverseRange(key, start, end);
    }

    public Set<String> rangeByScore(String key, double min, double max) {
        return zSetOperations.rangeByScore(key, min, max);
    }

    public Set<String> reverseByScore(String key, double min, double max) {
        return zSetOperations.reverseRangeByScore(key, min, max);
    }

    public Long count(String key, double min, double max) {
        return zSetOperations.count(key, min, max);
    }

    public Long zSetSize(String key) {
        return zSetOperations.size(key);
    }


    public Double score(String key, Object value) {
        return zSetOperations.score(key, value);
    }


    public Long removeRange(String key, long start, long end) {
        return zSetOperations.removeRange(key, start, end);
    }

    public Long removeRangeByScore(String key, double min, double max) {
        return zSetOperations.removeRangeByScore(key, min, max);
    }

    public void expire(String key, long time, TimeUnit timeUnit) {
        redisTemplate.expire(key, time, timeUnit);
    }

    public void deleteKey(String key) {
        this.redisTemplate.delete(key);
    }

    public Boolean hasKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public void deleteKey(List<String> keys) {
        this.redisTemplate.delete(keys);
    }

    public Long getExpire(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    public void sendMessage(String channel, String message) {
        getStringRedisTemplate().convertAndSend(channel, message);
    }


    public List<Object> executePipelined(Consumer<RedisOperations> redisOperationsConsumer) {
        return getStringRedisTemplate().executePipelined(new SessionCallback<Object>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                redisOperationsConsumer.accept(operations);
                return null;
            }
        });
    }

}

