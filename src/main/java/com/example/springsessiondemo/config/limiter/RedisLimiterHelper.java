package com.example.springsessiondemo.config.limiter;

import org.apache.commons.lang3.BooleanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/6/19.
 * Time:19:08
 */
public class RedisLimiterHelper {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private final DefaultRedisScript<Boolean> luaLimiterRedisScript;


    public RedisLimiterHelper() {
        luaLimiterRedisScript = new DefaultRedisScript<>();
        luaLimiterRedisScript.setResultType(Boolean.class);
        luaLimiterRedisScript.setLocation(new ClassPathResource("script/RedisLimiterLuaScript.lua"));
    }


    public boolean tryAcquire(String limitKey, Integer count, Integer internal, TimeUnit timeUnit) {
        try {
            List<String> keys = Collections.singletonList(limitKey);
            Boolean result = stringRedisTemplate.execute(luaLimiterRedisScript, keys, String.valueOf(count), String.valueOf(timeUnit.toSeconds(internal)));
            return BooleanUtils.toBoolean(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


}
