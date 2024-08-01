package com.dingyabin.springsecuritydemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// 优质的 spring/boot/data/security/cloud 框架中文文档尽在 => https://springdoc.cn
@MapperScan("com.dingyabin.security.mapper")
@SpringBootApplication(scanBasePackages = {"com.dingyabin.springsecuritydemo", "com.dingyabin.security"})
public class SpringSecurityDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityDemoApplication.class, args);
    }

}
