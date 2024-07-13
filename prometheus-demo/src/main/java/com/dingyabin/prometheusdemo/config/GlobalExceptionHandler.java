package com.dingyabin.prometheusdemo.config;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:16:22
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public String globalExceptionHandler() {
        return "bad";
    }
}
