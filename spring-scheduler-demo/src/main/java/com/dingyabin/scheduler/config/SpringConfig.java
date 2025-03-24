package com.dingyabin.scheduler.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {


    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }


}
