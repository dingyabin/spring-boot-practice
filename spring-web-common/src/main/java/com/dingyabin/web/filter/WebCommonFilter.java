package com.dingyabin.web.filter;

import cn.hutool.extra.servlet.ServletUtil;
import com.dingyabin.web.property.WebConfigProperty;
import com.dingyabin.web.request.RepeatReadHttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Slf4j
public class WebCommonFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";

    private final WebConfigProperty webConfigProperty;

    public WebCommonFilter(WebConfigProperty webConfigProperty) {
        this.webConfigProperty = webConfigProperty;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //自动注入traceId
        if (webConfigProperty.isAutoInjectTraceId()) {
            autoInjectTraceId(request, response);
        }
        //替换为可重复读的request
        if(webConfigProperty.isRepeatReadRequestProxy()) {
            request = new RepeatReadHttpServletRequest(request);
        }
        //记录开始时间
        long startTime = webConfigProperty.isLogTrace() ? System.currentTimeMillis() : 0;

        filterChain.doFilter(request, response);
        //记录日志
        if (webConfigProperty.isLogTrace()) {
            logTrace(request, startTime);
        }
    }


    private void autoInjectTraceId(HttpServletRequest request, HttpServletResponse response) {
        //先从header获取
        String traceId = request.getHeader(TRACE_ID);
        if (!StringUtils.hasText(traceId)) {
            //再从参数获取
            traceId = request.getParameter(TRACE_ID);
        }
        if (!StringUtils.hasText(traceId)) {
            //生成一个
            traceId = UUID.randomUUID().toString().replace("-", "");
        }
        MDC.put(TRACE_ID, traceId);
        response.addHeader(TRACE_ID, traceId);
    }



    private void logTrace(HttpServletRequest request, long startTime) throws IOException {
        long costTime = System.currentTimeMillis() - startTime;
        String url = request.getRequestURL().toString();
        Map<String, String> params = ServletUtil.getParamMap(request);
        String jsonBody = null;
        if (request instanceof RepeatReadHttpServletRequest) {
            jsonBody = ((RepeatReadHttpServletRequest) request).getJsonBody();
        }
        String clientIP = null;
        if (webConfigProperty.isPrintIp()) {
            clientIP = ServletUtil.getClientIP(request);
        }
        Map<String, String> headerMap = null;
        if (webConfigProperty.isPrintHeader()) {
             headerMap = ServletUtil.getHeaderMap(request);
        }
        log.info("http请求 IP={}, URL={}, header={}, params={}, jsonBody={}, 总耗时={}ms", clientIP, url, headerMap, params, jsonBody, costTime);
    }


}