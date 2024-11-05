package com.dingyabin.web.config.strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 丁亚宾
 * Date: 2024/11/5.
 * Time:22:07
 */
public interface TraceIdStyleStrategy {

    String traceId(HttpServletRequest request, HttpServletResponse response);
}
