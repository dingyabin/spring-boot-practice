package com.dingyabin.captcha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dingyabin.captcha.mapper")
public class CaptchaAppMain {

    public static void main(String[] args) {
        SpringApplication.run(CaptchaAppMain.class, args);
    }
}