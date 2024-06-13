package com.example.springsessiondemo.intecepter;

import com.alibaba.fastjson.JSONObject;
import com.example.springsessiondemo.annotation.NoNeedLogin;
import com.example.springsessiondemo.context.UserContext;
import com.example.springsessiondemo.web.User;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author 丁亚宾
 * Date: 2024/6/13.
 * Time:20:13
 */
public class LoginCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否需要校验登录, @NoNeedLogin不需要登录校验
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(NoNeedLogin.class) || handlerMethod.getBeanType().isAnnotationPresent(NoNeedLogin.class)) {
            return true;
        }
        //下面的都需要校验登录
        HttpSession session = request.getSession(false);
        Object user;
        if (session == null || (user = session.getAttribute("user")) == null) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("未登录!");
            return false;
        }
        UserContext.setCurrentUser(((JSONObject) user).toJavaObject(User.class));
        return true;
    }





    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.removeUser();
    }
}
