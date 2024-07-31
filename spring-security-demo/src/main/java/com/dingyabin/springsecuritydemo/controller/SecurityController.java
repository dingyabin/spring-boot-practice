package com.dingyabin.springsecuritydemo.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingyabin.response.Result;
import com.dingyabin.springsecuritydemo.config.security.SecurityUserDetails;
import com.dingyabin.springsecuritydemo.enums.RedisKeyEnum;
import com.dingyabin.springsecuritydemo.model.reqest.LoginRequest;
import com.dingyabin.springsecuritydemo.model.response.SecurityUserCache;
import com.dingyabin.springsecuritydemo.model.response.TokenMsg;
import com.dingyabin.springsecuritydemo.util.JwtUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:44
 */
@RestController
public class SecurityController {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @PostMapping("/login")
    public Result<Object> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUserName(), loginRequest.getPwd());
        Authentication authenticate = authenticationManager.authenticate(unauthenticated);

        if (authenticate.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            SecurityUserDetails userDetails = (SecurityUserDetails) authenticate.getPrincipal();

            String key = RedisKeyEnum.LOGIN_USER.toKey(userDetails.getSysUser().getId());
            stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(new SecurityUserCache(userDetails)), Duration.ofHours(2));
            String token = JwtUtils.getToken(new TokenMsg(userDetails.getSysUser().getId(), userDetails.getUsername()));
            //放入redis
            return Result.success(MapUtil.of("token", token));
        }
        return Result.fail(401, "认证失败！");
    }


    @PostMapping("/info")
    public Result<Object> info() {
        return Result.success("ok!");
    }
}
