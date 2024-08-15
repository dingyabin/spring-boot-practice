package com.dingyabin.redis;

import com.dingyabin.redis.helper.RedisHelper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 丁亚宾
 * Date: 2024/8/16.
 * Time:1:53
 */
@SpringBootTest
public class RedisTest {

    @Resource
    private RedisHelper redisHelper;

    @Test
    public void test() {

        Map<String, String> map = redisHelper.getCacheMap("map");
        System.out.println(map);
    }

}
