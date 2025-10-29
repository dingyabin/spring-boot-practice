package com.dingyabin.captcha.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface KaptchaGenerateService {

    void createKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response);

}
