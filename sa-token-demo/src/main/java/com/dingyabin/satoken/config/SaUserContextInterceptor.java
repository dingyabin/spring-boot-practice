package com.dingyabin.satoken.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import com.dingyabin.satoken.model.LoginUserCache;
import com.dingyabin.satoken.util.UserContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 丁亚宾
 * Date: 2024/8/7.
 * Time:1:04
 */
public class SaUserContextInterceptor extends SaInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object loginUser = StpUtil.getSession().get("LoginUser");
        if (loginUser instanceof LoginUserCache) {
            LoginUserCache user = (LoginUserCache) loginUser;
            System.out.println("loginUser= " + user);
            UserContextHolder.setRequestUser(user);
        }
        return super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        UserContextHolder.remove();
    }
}
