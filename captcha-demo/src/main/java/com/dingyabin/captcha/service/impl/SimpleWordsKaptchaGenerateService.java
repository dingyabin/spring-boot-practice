package com.dingyabin.captcha.service.impl;

import com.dingyabin.captcha.service.KaptchaGenerateService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class SimpleWordsKaptchaGenerateService implements KaptchaGenerateService {


    @Resource
    private DefaultKaptcha defaultKaptcha;


    @Override
    public void createKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse response) {

    }
}
