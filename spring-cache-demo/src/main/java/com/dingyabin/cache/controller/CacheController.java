package com.dingyabin.cache.controller;

import com.dingyabin.cache.model.Student;
import com.dingyabin.cache.service.CacheService;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/8/22.
 * Time:23:50
 */
@RestController
public class CacheController {


    @Resource
    private CacheService cacheService;


    @PostMapping("/test1")
    public Result<Student> testCache(String name) {
        Student student = cacheService.getStudent(name);
        return Result.success(student);
    }

}
