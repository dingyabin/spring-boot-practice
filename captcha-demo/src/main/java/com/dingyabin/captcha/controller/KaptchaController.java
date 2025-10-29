package com.dingyabin.captcha.controller;

import com.dingyabin.captcha.service.KaptchaGenerateService;
import com.dingyabin.response.Result;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:21:31
 */
@RestController
public class KaptchaController {


    @Resource(name = "clickWordsKaptchaGenerateService")
    private KaptchaGenerateService kaptchaGenerateService;


    @RequestMapping("/get")
    public void createKaptcha(HttpServletRequest request, HttpServletResponse response) {
        kaptchaGenerateService.createKaptcha(request, response);
    }


    @RequestMapping("/get2")
    public Result<String> createKaptcha2(HttpServletRequest request, HttpServletResponse response) {
        String base64Kaptcha = kaptchaGenerateService.createBase64Kaptcha(request, response);
        return Result.success(base64Kaptcha);
    }
}
