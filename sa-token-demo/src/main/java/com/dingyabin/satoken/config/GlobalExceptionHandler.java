package com.dingyabin.satoken.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author 丁亚宾
 * Date: 2024/8/7.
 * Time:0:35
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 全局异常拦截
    @ExceptionHandler(NotLoginException.class)
    public Result<String> handlerException(NotLoginException e) {
        return Result.fail(301, "需要先登录");
    }


    @ExceptionHandler(NotRoleException.class)
    public Result<String> handlerException2(NotRoleException e) {
        return Result.fail(301, "权限不足");
    }

    @ExceptionHandler(NotPermissionException.class)
    public Result<String> handlerException3(NotPermissionException e) {
        return Result.fail(301, "权限不足");
    }

}
