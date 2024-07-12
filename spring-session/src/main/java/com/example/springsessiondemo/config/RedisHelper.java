package com.example.springsessiondemo.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.*;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/6/30.
 * Time:22:42
 */
public class RedisHelper {

    private static final Logger log = LoggerFactory.getLogger(RedisHelper.class);

    protected RedisTemplate redisTemplate;

//    public RedisHelper(RedisTemplate redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    public void setCache(String key, Object value, Integer timeout) {
//        this.setCache(key, value, timeout, TimeUnit.SECONDS);
//    }
//
//    public void setCache(String key, Object value, Integer timeout, TimeUnit timeUnit) {
//        this.redisTemplate.opsForValue().set(key, this.toJSONString(value), (long)timeout, timeUnit);
//    }
//
//    public Long increment(String key, Integer timeout) {
//        return this.increment(key, 1L, timeout, TimeUnit.SECONDS);
//    }
//
//    public Long increment(String key, long delta, Integer timeout, TimeUnit timeUnit) {
//        ValueOperations operation = this.redisTemplate.opsForValue();
//        Long increment = operation.increment(key, delta);
//        this.expire(key, (long)timeout, timeUnit);
//        return increment;
//    }
//
//    public Double increment(String key, double delta, Integer timeout, TimeUnit timeUnit) {
//        ValueOperations operation = this.redisTemplate.opsForValue();
//        Double increment = operation.increment(key, delta);
//        this.expire(key, (long)timeout, timeUnit);
//        return increment;
//    }
//
//    public Long decrement(String key, Integer timeout) {
//        return this.decrement(key, 1L, timeout, TimeUnit.SECONDS);
//    }
//
//    public Long decrement(String key, long delta, Integer timeout, TimeUnit timeUnit) {
//        ValueOperations operation = this.redisTemplate.opsForValue();
//        Long decrement = operation.decrement(key, delta);
//        this.expire(key, (long)timeout, timeUnit);
//        return decrement;
//    }
//
//    public <T> T getCacheObject(String key, Class<T> clazz) {
//        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
//        String value = (String)valueOperations.get(key);
//        return value == null ? null : this.toJavaObject(value, clazz);
//    }
//
//    public String getCacheString(String key) {
//        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
//        return (String)valueOperations.get(key);
//    }
//
//    public List<String> multiGetString(List<String> keys) {
//        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
//        return valueOperations.multiGet(keys);
//    }
//
//    public <T> List<T> multiGetObject(List<String> keys, Class<T> clazz) {
//        ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();
//        List<String> strings = valueOperations.multiGet(keys);
//        return CollectionUtils.isEmpty(strings) ? Collections.EMPTY_LIST : this.toJavaList(strings, clazz);
//    }
//
//    public Boolean lock(String key, Object value, Integer timeout, TimeUnit timeUnit) {
//        return this.redisTemplate.opsForValue().setIfAbsent(key, this.toJSONString(value), (long)timeout, timeUnit);
//    }
//
//    public void trim(String key, long start, long end) {
//        this.redisTemplate.boundListOps(key).trim(start, end);
//    }
//
//    public void set(String key, long index, Object value) {
//        BoundListOperations boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.set(index, this.toJSONString(value));
//    }
//
//    public void leftPush(String key, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.leftPush(this.toJSONString(value));
//    }
//
//    public void leftPush(String key, Object pivot, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.leftPush(this.toJSONString(pivot), this.toJSONString(value));
//    }
//
//    public void leftPushIfPresent(String key, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.leftPushIfPresent(this.toJSONString(value));
//    }
//
//    public void rightPush(String key, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.rightPush(this.toJSONString(value));
//    }
//
//    public void rightPush(String key, Object pivot, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.rightPush(this.toJSONString(pivot), this.toJSONString(value));
//    }
//
//    public void rightPushIfPresent(String key, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.rightPushIfPresent(this.toJSONString(value));
//    }
//
//    public void remove(String key, long count, Object value) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        boundListOperations.remove(count, this.toJSONString(value));
//    }
//
//    public <T> void leftPushAll(String key, List<T> dataList, Integer timeout) {
//        this.leftPushAll(key, dataList, timeout, TimeUnit.SECONDS);
//    }
//
//    public <T> void leftPushAll(String key, List<T> dataList, Integer timeout, TimeUnit timeUnit) {
//        if (!CollectionUtils.isEmpty(dataList)) {
//            int size = dataList.size();
//            if (size > 8000) {
//                log.info("Redis集合类型leftPushAll集合对象大于8000，缓存Key:{}", key);
//            }
//
//            if (size > 10000) {
//                log.info("Redis集合类型leftPushAll集合对象大于10000，缓存Key:{}", key);
//                if (!this.hasKey(key)) {
//                    BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//                    boundListOperations.leftPush(this.toJSONString(dataList.get(0)));
//                }
//
//            } else {
//                List<Object> list = new ArrayList();
//
//                for(int i = 0; i < size; ++i) {
//                    list.add(this.toJSONString(dataList.get(i)));
//                }
//
//                ListOperations<String, Object> listOperation = this.redisTemplate.opsForList();
//                listOperation.leftPushAll(key, list);
//                this.expire(key, (long)timeout, timeUnit);
//            }
//        }
//    }
//
//    public <T> void rightPushAll(String key, List<T> dataList, Integer timeout) {
//        this.rightPushAll(key, dataList, timeout, TimeUnit.SECONDS);
//    }
//
//    public <T> void rightPushAll(String key, List<T> dataList, Integer timeout, TimeUnit timeUnit) {
//        if (!CollectionUtils.isEmpty(dataList)) {
//            int size = dataList.size();
//            if (size > 8000) {
//                log.info("Redis集合类型rightPushAll集合对象大于8000，缓存Key:{}", key);
//            }
//
//            if (size > 10000) {
//                log.info("Redis集合类型rightPushAll集合对象大于10000，缓存Key:{}", key);
//                if (!this.hasKey(key)) {
//                    BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//                    boundListOperations.rightPush(this.toJSONString(dataList.get(0)));
//                }
//
//            } else {
//                List<Object> list = new ArrayList();
//
//                for(int i = 0; i < size; ++i) {
//                    list.add(this.toJSONString(dataList.get(i)));
//                }
//
//                ListOperations<String, Object> listOperation = this.redisTemplate.opsForList();
//                listOperation.rightPushAll(key, list);
//                this.expire(key, (long)timeout, timeUnit);
//            }
//        }
//    }
//
//    public <T> List<T> listRange(String key, Class<T> clazz) {
//        return this.listRange(key, clazz, 0L, -1L);
//    }
//
//    public <T> List<T> listRange(String key, Class<T> clazz, long start, long end) {
//        ListOperations<String, Object> listOperation = this.redisTemplate.opsForList();
//        List<Object> dataList = listOperation.range(key, start, end);
//        return this.toJavaList(dataList, clazz);
//    }
//
//    public Long listSize(String key) {
//        return this.redisTemplate.opsForList().size(key);
//    }
//
//    public <T> T index(String key, Class<T> clazz, long index) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        Object object = boundListOperations.index(index);
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public <T> T leftPop(String key, Class<T> clazz) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        Object object = boundListOperations.leftPop();
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public <T> T leftPop(String key, Class<T> clazz, long timeout, TimeUnit unit) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        Object object = boundListOperations.leftPop(timeout, unit);
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public <T> T rightPop(String key, Class<T> clazz) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        Object object = boundListOperations.rightPop();
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public <T> T rightPop(String key, Class<T> clazz, long timeout, TimeUnit unit) {
//        BoundListOperations<String, Object> boundListOperations = this.redisTemplate.boundListOps(key);
//        Object object = boundListOperations.rightPop(timeout, unit);
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public Long delete(String key, Object... hashKeys) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        return hashOperations.delete(key, hashKeys);
//    }
//
//    public Boolean hasKey(String key, Object hashKey) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        return hashOperations.hasKey(key, this.toJSONString(hashKey));
//    }
//
//    public <T> T get(String key, Class<T> clazz, Object hashKey) {
//        HashOperations<String, Object, Object> hashOperations = this.redisTemplate.opsForHash();
//        Object object = hashOperations.get(key, this.toJSONString(hashKey));
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public <T> List<T> multiGet(String key, Class<T> clazz, List<String> hashKeys) {
//        HashOperations<String, String, Object> hashOperations = this.redisTemplate.opsForHash();
//        List<Object> objects = hashOperations.multiGet(key, hashKeys);
//        return this.toJavaList(objects, clazz);
//    }
//
//    public Long increment(String key, Object hashKey, long delta) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        return hashOperations.increment(key, this.toJSONString(hashKey), delta);
//    }
//
//    public Double increment(String key, Object hashKey, double delta) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        return hashOperations.increment(key, this.toJSONString(hashKey), delta);
//    }
//
//    public <T> Set<T> keys(String key) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        return hashOperations.keys(key);
//    }
//
//    public Long hashSize(String key) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        return hashOperations.size(key);
//    }
//
//    public <T> void putAll(String key, Map<String, T> dataMap, Integer timeout) {
//        this.putAll(key, dataMap, timeout, TimeUnit.SECONDS);
//    }
//
//    public <T> void putAll(String key, Map<String, T> dataMap, Integer timeout, TimeUnit timeUnit) {
//        if (!CollectionUtils.isEmpty(dataMap)) {
//            Map<String, Object> data = new HashMap(16);
//            Iterator var6 = dataMap.entrySet().iterator();
//
//            while(var6.hasNext()) {
//                Map.Entry<String, T> entry = (Map.Entry)var6.next();
//                data.put(this.toJSONString(entry.getKey()), this.toJSONString(entry.getValue()));
//            }
//
//            HashOperations hashOperations = this.redisTemplate.opsForHash();
//            hashOperations.putAll(key, data);
//            this.expire(key, (long)timeout, timeUnit);
//        }
//    }
//
//    public <T> void put(String key, Object haskey, T value, Integer timeout) {
//        this.put(key, haskey, value, timeout, TimeUnit.SECONDS);
//    }
//
//    public <T> void put(String key, Object haskey, T value, Integer timeout, TimeUnit timeUnit) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        hashOperations.put(key, this.toJSONString(haskey), this.toJSONString(value));
//        this.expire(key, (long)timeout, timeUnit);
//    }
//
//    public <T> void put(String key, Object haskey, T value) {
//        HashOperations hashOperations = this.redisTemplate.opsForHash();
//        hashOperations.put(key, this.toJSONString(haskey), this.toJSONString(value));
//    }
//
//    public <T> Map<String, T> getCacheMap(String key, Class<T> clazz) {
//        Map<String, Object> map = this.redisTemplate.opsForHash().entries(key);
//        if (CollectionUtils.isEmpty(map)) {
//            return null;
//        } else {
//            Map<String, T> data = new HashMap(16);
//            Iterator var5 = map.entrySet().iterator();
//
//            while(var5.hasNext()) {
//                Map.Entry<String, Object> entry = (Map.Entry)var5.next();
//                data.put(entry.getKey(), this.toJavaObject(entry.getValue().toString(), clazz));
//            }
//
//            return data;
//        }
//    }
//
//    public Map<Object, Object> getCacheMap(String key) {
//        Map<Object, Object> map = this.redisTemplate.opsForHash().entries(key);
//        return map;
//    }
//
//    public <T> void addAll(String key, Set<T> dataSet, Integer timeout) {
//        this.addAll(key, dataSet, timeout, TimeUnit.SECONDS);
//    }
//
//    public <T> void addAll(String key, Set<T> dataSet, Integer timeout, TimeUnit timeUnit) {
//        if (!CollectionUtils.isEmpty(dataSet)) {
//            Set<Object> set = new HashSet();
//            dataSet.forEach((e) -> {
//                set.add(this.toJSONString(e));
//            });
//            Object[] objects = set.toArray();
//            BoundSetOperations<String, Object> setOperation = this.redisTemplate.boundSetOps(key);
//            setOperation.add(objects);
//            this.expire(key, (long)timeout, timeUnit);
//        }
//    }
//
//    public <T> Set<T> getCacheSet(String key, Class<T> clazz) {
//        BoundSetOperations<String, Object> operation = this.redisTemplate.boundSetOps(key);
//        Set<Object> members = operation.members();
//        return this.toJavaSet(members, clazz);
//    }
//
//    public Long setRemove(String key, Object... values) {
//        BoundSetOperations<String, Object> setOperation = this.redisTemplate.boundSetOps(key);
//        Long remove = setOperation.remove(values);
//        return remove;
//    }
//
//    public <T> T pop(String key, Class<T> clazz) {
//        BoundSetOperations<String, Object> setOperation = this.redisTemplate.boundSetOps(key);
//        Object object = setOperation.pop();
//        return null == object ? null : this.toJavaObject(object.toString(), clazz);
//    }
//
//    public <T> List<T> pop(String key, Class<T> clazz, long count) {
//        SetOperations setOperations = this.redisTemplate.opsForSet();
//        List<Object> pop = setOperations.pop(key, count);
//        return this.toJavaList(pop, clazz);
//    }
//
//    public void pop(String key, long count) {
//        SetOperations setOperations = this.redisTemplate.opsForSet();
//        setOperations.pop(key, count);
//    }
//
//    public Long size(String key) {
//        SetOperations setOperations = this.redisTemplate.opsForSet();
//        return setOperations.size(key);
//    }
//
//    public <T> Set<T> randomMembers(String key, Class<T> clazz, long count) {
//        SetOperations setOperations = this.redisTemplate.opsForSet();
//        Set<Object> randomMembers = setOperations.distinctRandomMembers(key, count);
//        return this.toJavaSet(randomMembers, clazz);
//    }
//
//    public Boolean isMember(String key, Object value) {
//        SetOperations setOperations = this.redisTemplate.opsForSet();
//        return setOperations.isMember(key, this.toJSONString(value));
//    }
//
//    public <T> void add(String key, T value, double score) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        zSetOperations.add(this.toJSONString(value), score);
//    }
//
//    public Double incrementScore(String key, Object value, double delta) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        return zSetOperations.incrementScore(this.toJSONString(value), delta);
//    }
//
//    public Long zSetRemove(String key, Object... value) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        return zSetOperations.remove(value);
//    }
//
//    public Long rank(String key, Object value) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Long rank = zSetOperations.rank(this.toJSONString(value));
//        return rank;
//    }
//
//    public Long reverseRank(String key, Object value) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Long rank = zSetOperations.reverseRank(this.toJSONString(value));
//        return rank;
//    }
//
//    public <T> Set<T> zSetRange(String key, Class<T> clazz) {
//        return this.zSetRange(key, clazz, 0L, -1L);
//    }
//
//    public <T> Set<T> zSetRange(String key, Class<T> clazz, long start, long end) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Set<Object> range = zSetOperations.range(start, end);
//        return this.toJavaSet(range, clazz);
//    }
//
//    public <T> Set<T> reverseRange(String key, Class<T> clazz, long start, long end) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Set<Object> range = zSetOperations.reverseRange(start, end);
//        return this.toJavaSet(range, clazz);
//    }
//
//    public <T> Set<T> rangeByScore(String key, Class<T> clazz, double min, double max) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Set<Object> range = zSetOperations.rangeByScore(min, max);
//        return this.toJavaSet(range, clazz);
//    }
//
//    public <T> Set<T> reverseByScore(String key, Class<T> clazz, double min, double max) {
//        BoundZSetOperations<String, Object> zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Set<Object> range = zSetOperations.reverseRangeByScore(min, max);
//        return this.toJavaSet(range, clazz);
//    }
//
//    public Long count(String key, double min, double max) {
//        BoundZSetOperations zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Long count = zSetOperations.count(min, max);
//        return count;
//    }
//
//    public Long zSetSize(String key) {
//        BoundZSetOperations zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Long count = zSetOperations.size();
//        return count;
//    }
//
//    public Double score(String key, Object value) {
//        BoundZSetOperations zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Double score = zSetOperations.score(this.toJSONString(value));
//        return score;
//    }
//
//    public Long removeRange(String key, long start, long end) {
//        BoundZSetOperations zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Long aLong = zSetOperations.removeRange(start, end);
//        return aLong;
//    }
//
//    public Long removeRangeByScore(String key, double min, double max) {
//        BoundZSetOperations zSetOperations = this.redisTemplate.boundZSetOps(key);
//        Long aLong = zSetOperations.removeRangeByScore(min, max);
//        return aLong;
//    }
//
//    public boolean expire(String key, long time, TimeUnit timeUnit) {
//        return this.redisTemplate.expire(key, time, timeUnit);
//    }
//
//    public void deleteKey(String key) {
//        this.redisTemplate.delete(key);
//    }
//
//    public Boolean hasKey(String key) {
//        return this.redisTemplate.hasKey(key);
//    }
//
//    public void deleteKey(List<String> keys) {
//        this.redisTemplate.delete(keys);
//    }
//
//    public Long getExpire(String key) {
//        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
//    }
}
