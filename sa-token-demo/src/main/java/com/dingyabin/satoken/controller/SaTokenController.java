package com.dingyabin.satoken.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * Date: 2024/8/2.
 * Time:22:08
 */
@RestController
public class SaTokenController {



    @PostMapping("/login")
    public Result<Object> login() {
        StpUtil.login(10001);
        String tokenValue = StpUtil.getTokenValue();
        System.out.println(tokenValue);
        return Result.success("登录成功!");
    }

}
