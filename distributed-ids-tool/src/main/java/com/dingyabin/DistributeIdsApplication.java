package com.dingyabin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan({"com.dingyabin.distributeId.mapper","com.dingyabin.captcha.mapper"})
public class DistributeIdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DistributeIdsApplication.class, args);
    }

}
