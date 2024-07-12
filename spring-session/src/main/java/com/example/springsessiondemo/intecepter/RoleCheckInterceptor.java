package com.example.springsessiondemo.intecepter;

import com.alibaba.fastjson.JSONObject;
import com.example.springsessiondemo.annotation.HasRole;
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
public class RoleCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否需要校验权限, @HasRole需要校验
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (!handlerMethod.hasMethodAnnotation(HasRole.class) && !handlerMethod.getBeanType().isAnnotationPresent(HasRole.class)) {
            return true;
        }
        //方法上的覆盖类上的
        HasRole hasRoleAnnotation = handlerMethod.getMethodAnnotation(HasRole.class);
        if (hasRoleAnnotation == null) {
            hasRoleAnnotation = handlerMethod.getBeanType().getAnnotation(HasRole.class);
        }
        //校验
        String[] hasRoles = hasRoleAnnotation.hasRoles();
        HasRole.HasRoleCheckType hasRoleCheckType = hasRoleAnnotation.checkType();
        if (!hasRoleCheckType.doCheck(hasRoles, UserContext.getCurrentUser())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("权限不足!");
            return false;
        }
        return true;
    }
}
