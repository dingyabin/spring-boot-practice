package com.dingyabin.springsecuritydemo.config.security;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.dingyabin.response.Result;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:10
 */
@Component
public class CustomerAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Result<Object> result = Result.fail(401, "认证失败, 账号或密码错误！");
        response.setCharacterEncoding("utf-8");
        ServletUtil.write(response, JSONObject.toJSONString(result), MediaType.APPLICATION_JSON_VALUE);
    }
}
