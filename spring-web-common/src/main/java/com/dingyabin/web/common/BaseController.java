package com.dingyabin.web.common;

import com.dingyabin.response.Result;

/**
 * @author 丁亚宾
 * Date: 2024/11/5.
 * Time:0:21
 */
public abstract class BaseController{

    public static final String FALLBACK_METHOD_NAME = "commonFallbackMethod";

    private Result<String> commonFallbackMethod(Exception exception) {
        return Result.fail(503, "系统繁忙，请稍后再试...");
    }

}
