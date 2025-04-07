package com.dingyabin.redis.limiter;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RedisLimiterHelper {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    private final DefaultRedisScript<Long> luaLimiterRedisScript;


    public RedisLimiterHelper() {
        luaLimiterRedisScript = new DefaultRedisScript<>();
        luaLimiterRedisScript.setResultType(Long.class);
        luaLimiterRedisScript.setLocation(new ClassPathResource("script/RedisLimiterLuaScript.lua"));
    }


    public boolean tryAcquire(String limitKey, Integer count, Integer internal, TimeUnit timeUnit) {
        try {
            List<String> keys = Collections.singletonList(limitKey);
            Long result = stringRedisTemplate.execute(luaLimiterRedisScript, keys, String.valueOf(timeUnit.toSeconds(internal)));
            return result != null && result <= count;
        } catch (Exception e) {
            log.error("RedisLimiterHelper error...", e);
        }
        return true;
    }


}
