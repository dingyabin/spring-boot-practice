package com.example.springsessiondemo.intecepter;

import com.example.springsessiondemo.annotation.HasPermit;
import com.example.springsessiondemo.context.UserContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 丁亚宾
 * Date: 2024/6/13.
 * Time:20:13
 */
public class PermitCheckInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断是否需要校验权限, @HasPermit需要校验
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (!handlerMethod.hasMethodAnnotation(HasPermit.class) && !handlerMethod.getBeanType().isAnnotationPresent(HasPermit.class)) {
            return true;
        }
        //方法上的覆盖类上的
        HasPermit hasPermitAnnotation = handlerMethod.getMethodAnnotation(HasPermit.class);
        if (hasPermitAnnotation == null) {
            hasPermitAnnotation = handlerMethod.getBeanType().getAnnotation(HasPermit.class);
        }
        //校验
        String[] hasPermit = hasPermitAnnotation.hasPermit();
        HasPermit.HasPermitCheckType hasPermitCheckType = hasPermitAnnotation.checkType();
        if (!hasPermitCheckType.doCheck(hasPermit, UserContext.getCurrentUser())) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println("权限不足!");
            return false;
        }
        return true;
    }
}
