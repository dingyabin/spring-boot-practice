package com.dingyabin.lock.controller;

import com.dingyabin.lock.service.LockService;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:21:31
 */
@RestController
public class LockController {

    @Resource
    private LockService lockService;

    @GetMapping("/get")
    public Result<String> lock() {
        lockService.annotationLock(10L);
        return Result.success("ok");
    }


    @GetMapping("/get2")
    public Result<String> lock2() {
        lockService.manualLock();
        return Result.success("ok");
    }


}
