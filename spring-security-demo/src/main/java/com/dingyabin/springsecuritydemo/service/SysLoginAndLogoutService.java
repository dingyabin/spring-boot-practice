package com.dingyabin.springsecuritydemo.service;

import com.alibaba.fastjson.JSONObject;
import com.dingyabin.springsecuritydemo.config.security.SecurityUserDetails;
import com.dingyabin.springsecuritydemo.enums.RedisKeyEnum;
import com.dingyabin.springsecuritydemo.model.reqest.LoginRequest;
import com.dingyabin.springsecuritydemo.model.response.LoginResponse;
import com.dingyabin.springsecuritydemo.model.response.SecurityUserCache;
import com.dingyabin.springsecuritydemo.model.response.TokenMsg;
import com.dingyabin.springsecuritydemo.util.JwtUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;

import static org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:18:02
 */
@Service
public class SysLoginAndLogoutService {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public LoginResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken unauthenticated = unauthenticated(loginRequest.getUserName(), loginRequest.getPwd());
        Authentication authenticate = authenticationManager.authenticate(unauthenticated);
        if (authenticate.isAuthenticated()) {
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            SecurityUserDetails userDetails = (SecurityUserDetails) authenticate.getPrincipal();
            //放入redis
            cacheSecurityUser(userDetails);
            //生成token
            String token = JwtUtils.getToken(new TokenMsg(userDetails.getSysUser().getId(), userDetails.getUsername()));
            return new LoginResponse(token);
        }
        return new LoginResponse();
    }


    /**
     * 把当前用户放入缓存
     * @param userDetails 当前用户
     */
    private void cacheSecurityUser(SecurityUserDetails userDetails) {
        String key = RedisKeyEnum.LOGIN_USER.toKey(userDetails.getSysUser().getId());
        stringRedisTemplate.opsForValue().set(key, JSONObject.toJSONString(new SecurityUserCache(userDetails)), Duration.ofHours(2));
    }

    /**
     * 删除当前用户
     * @param userDetails 当前用户
     */
    private void delSecurityUserCache(SecurityUserDetails userDetails) {
        String key = RedisKeyEnum.LOGIN_USER.toKey(userDetails.getSysUser().getId());
        stringRedisTemplate.delete(key);
    }



    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return;
        }
        SecurityContextHolder.clearContext();
        delSecurityUserCache((SecurityUserDetails) authentication.getPrincipal());
    }


}
