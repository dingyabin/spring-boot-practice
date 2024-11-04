package com.dingyabin.resilience4j.controller;

import com.dingyabin.response.Result;
import com.dingyabin.web.common.BaseController;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 丁亚宾
 * Date: 2024/11/4.
 * Time:23:18
 */
@RestController
public class ResilienceController extends BaseController {


    @RequestMapping("/testRateLimiter")
    @RateLimiter(name = "controllerA", fallbackMethod = FALLBACK_METHOD_NAME)
    public Result<String> testRateLimiter(@RequestParam(value = "param", required = false) String param) {
        return Result.success("ok");
    }


}
