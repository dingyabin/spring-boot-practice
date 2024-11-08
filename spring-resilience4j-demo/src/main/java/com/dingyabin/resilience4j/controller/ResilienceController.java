package com.dingyabin.resilience4j.controller;

import com.dingyabin.resilience4j.model.Student;
import com.dingyabin.response.Result;
import com.dingyabin.web.common.BaseController;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Random;

/**
 * @author 丁亚宾
 * Date: 2024/11/4.
 * Time:23:18
 */
@RestController
public class ResilienceController extends BaseController {

    @Resource
    private RateLimiterRegistry rateLimiterRegistry;

    @Resource
    private RetryRegistry retryRegistry;


    @RequestMapping("/testRateLimiter")
    @RateLimiter(name = "controllerA", fallbackMethod = FALLBACK_METHOD_NAME)
    public Result<String> testRateLimiter(@RequestParam(value = "param", required = false) String param) {
        return Result.success("ok");
    }


    @RequestMapping("/testRateLimiter2")
    public Result<String> testRateLimiter2() {
        if (rateLimiterRegistry.rateLimiter("controllerA").acquirePermission()) {
            return Result.success("ok");
        }
        return Result.fail(100, "稍后再试...");
    }


    @Retry(name = "serviceA")
    @RequestMapping("/testRetry")
    public Result<String> testRetry() {
        System.out.println("开始执行");
        if (new Random().nextBoolean()) {
            throw new RuntimeException("失败了......");
        }
        return Result.success("ok");
    }

    @RequestMapping("/test1")
    public Result<Student> test1() {
        return Result.success(new Student());
    }
}
