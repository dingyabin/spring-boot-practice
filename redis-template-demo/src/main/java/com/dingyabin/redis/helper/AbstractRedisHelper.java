package com.dingyabin.redis.helper;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRedisHelper {

    protected StringRedisTemplate redisTemplate;


    public AbstractRedisHelper() {
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return this.redisTemplate;
    }


    public String toJson(Object object) {
        return object instanceof String ? object.toString() : JSONObject.toJSONString(object);
    }


    public <T> T toJavaObject(String json, Class<T> clazz) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        if (clazz.equals(String.class)) {
            return (T) json;
        }
        return JSONObject.parseObject(json, clazz);
    }



    public <T> List<T> toJavaList(List<String> dataList, Class<T> clazz) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        return dataList.stream().map(str -> toJavaObject(str, clazz)).collect(Collectors.toList());
    }



    public <T> List<String> toListString(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return Collections.emptyList();
        }
        return dataList.stream().map(this::toJson).collect(Collectors.toList());
    }

}