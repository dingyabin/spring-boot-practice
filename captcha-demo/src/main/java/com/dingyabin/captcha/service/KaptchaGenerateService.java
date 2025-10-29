package com.dingyabin.captcha.service;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface KaptchaGenerateService {

    void createKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response);

    String createBase64Kaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response);

    default void initImageResponseHeader(HttpServletResponse response){
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        response.setDateHeader(HttpHeaders.EXPIRES, 0);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        response.setHeader(HttpHeaders.PRAGMA, "no-cache");
    }
}
