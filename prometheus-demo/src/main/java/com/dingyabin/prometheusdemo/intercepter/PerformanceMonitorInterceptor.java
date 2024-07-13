package com.dingyabin.prometheusdemo.intercepter;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:16:05
 */
@Component
public class PerformanceMonitorInterceptor implements HandlerInterceptor {

    private final String needLogParam = "NEED_LOG";

    private final String STAR_TIME_PARAM = "_ST_";

    private final String END_TIME_PARAM = "_ET_";

    @Resource
    private MeterRegistry meterRegistry;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute(STAR_TIME_PARAM, System.currentTimeMillis());
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        request.setAttribute(END_TIME_PARAM, System.currentTimeMillis());

    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        Object st = request.getAttribute(STAR_TIME_PARAM);
        Object et = request.getAttribute(END_TIME_PARAM);
        if (st == null) {
            return;
        }
        if (et == null){
            System.out.println("请求失败了......");
            return;
        }
        long cost = (long) et - (long) st;
        System.out.println("cost:" + cost);

    }
}
