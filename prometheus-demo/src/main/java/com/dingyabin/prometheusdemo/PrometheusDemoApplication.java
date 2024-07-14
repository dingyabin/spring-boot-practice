package com.dingyabin.prometheusdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class PrometheusDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrometheusDemoApplication.class, args);
    }

}
