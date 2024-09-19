package com.dingyabin.nacos.controller;

import com.dingyabin.nacos.service.ConfigProperties;
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
public class ConfigController {

    @Resource
    private ConfigProperties configProperties;

    @GetMapping("/get")
    public Result<String> configTest(){
        return Result.success(configProperties.toString());
    }
}
