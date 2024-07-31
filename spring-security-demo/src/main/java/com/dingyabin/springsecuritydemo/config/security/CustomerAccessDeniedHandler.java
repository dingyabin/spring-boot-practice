package com.dingyabin.springsecuritydemo.config.security;

import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.dingyabin.response.Result;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:08
 */
@Component
public class CustomerAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        Result<Object> result = Result.fail(403, "无权访问！");
        response.setCharacterEncoding("utf-8");
        ServletUtil.write(response, JSONObject.toJSONString(result), MediaType.APPLICATION_JSON_VALUE);
    }

}
