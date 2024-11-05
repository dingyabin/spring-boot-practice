package com.dingyabin.web.config.strategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author 丁亚宾
 * Date: 2024/11/5.
 * Time:22:08
 */
public class UidTraceIdStyle implements TraceIdStyleStrategy {

    @Override
    public String traceId(HttpServletRequest request, HttpServletResponse response) {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
