package com.dingyabin.springsecuritydemo.controller;

import com.dingyabin.response.Result;
import com.dingyabin.springsecuritydemo.model.reqest.LoginRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:44
 */
@RestController
public class SecurityController {

    @Resource
    private AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public Result<Object> login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.getUserName(), loginRequest.getPwd());
        Authentication authenticate = authenticationManager.authenticate(unauthenticated);
        if (authenticate.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            Map<String, String> map = new HashMap<>();
            map.put("token", "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            return Result.success(map);
        }
        return Result.fail(401, "认证失败！");
    }
}
