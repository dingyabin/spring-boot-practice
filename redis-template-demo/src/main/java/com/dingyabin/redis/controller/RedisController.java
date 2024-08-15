package com.dingyabin.redis.controller;

import com.dingyabin.response.Result;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/8/7.
 * Time:0:15
 */
@RestController
public class RedisController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/send")
    public Result<Object> select(@RequestParam("data") String data) {
        stringRedisTemplate.convertAndSend("hello*", data);
        return Result.success();
    }

}
