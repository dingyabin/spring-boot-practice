package com.dingyabin.springsecuritydemo.controller;

import com.dingyabin.response.Result;
import com.dingyabin.springsecuritydemo.model.reqest.LoginRequest;
import com.dingyabin.springsecuritydemo.model.response.LoginResponse;
import com.dingyabin.springsecuritydemo.service.SysLoginAndLogoutService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:44
 */
@RestController
public class SecurityController {

    @Resource
    private SysLoginAndLogoutService sysLoginAndLogoutService;


    @PostMapping("/login")
    public Result<Object> login(LoginRequest loginRequest) {
        LoginResponse loginResponse = sysLoginAndLogoutService.login(loginRequest);
        if (loginResponse.getToken() != null) {
            return Result.success(loginResponse);
        }
        return Result.fail(401, "认证失败！");
    }



    @PostMapping("/logout")
    public Result<Object> logout() {
        sysLoginAndLogoutService.logout();
        return Result.success("退出成功!");
    }


    @PostMapping("/sys/info")
    public Result<Object> info() {
        return Result.success("ok!");
    }


    @PostMapping("/sys/update")
    public Result<Object> update() {
        return Result.success("ok!");
    }


    @PostMapping("/sys/save")
    public Result<Object> save() {
        return Result.success("ok!");
    }


    @PostMapping("/sys/select")
    public Result<Object> select() {
        return Result.success("ok!");
    }
}
