package com.dingyabin.satoken.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.stp.StpUtil;
import com.dingyabin.response.Result;
import com.dingyabin.satoken.model.LoginUserCache;
import com.dingyabin.satoken.util.UserContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/8/7.
 * Time:0:15
 */
@RestController
public class SaTokenController {


    @PostMapping("/login")
    public Result<String> doLogin(String name, String pwd) {
        // 第一步：比对前端提交的账号名称、密码
        if ("zhangsan".equals(name) && "999".equals(pwd)) {
            // 第二步：根据账号id，进行登录
            StpUtil.login(10001);
            createLoginUser();
            return Result.success("登录成功");
        }
        return Result.fail(301, "登录失败");
    }


    private void createLoginUser() {
        List<String> roleList = new ArrayList<>();
        roleList.add("admin");
        roleList.add("super-admin");

        List<String> authorityList = new ArrayList<>();
        authorityList.add("101");
        authorityList.add("user.add");

        LoginUserCache loginUserCache = new LoginUserCache().setUserId(10001L).setName("zhangsan").setRoleList(roleList).setAuthorityList(authorityList);
        StpUtil.getSession().set("LoginUser", loginUserCache);
    }



    @PostMapping("/logout")
    public Result<String> logout() {
        StpUtil.logout();
        return Result.success("注销成功");
    }

    ///////////////////////////////////////////////////////////////////////////
    @SaCheckLogin
    @PostMapping("/sys/info")
    public Result<Object> info() {
        return Result.success(UserContextHolder.getRequestUser());
    }


    @SaCheckRole("admin")
    @PostMapping("/sys/update")
    public Result<Object> update() {
        return Result.success(UserContextHolder.getRequestUser());
    }


    @PostMapping("/sys/save")
    public Result<Object> save() {
        return Result.success(UserContextHolder.getRequestUser());
    }


    @PostMapping("/sys/select")
    public Result<Object> select() {
        return Result.success(UserContextHolder.getRequestUser());
    }

}
