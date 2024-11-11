package com.dingyabin.scheduler;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;


/**
 * @author 丁亚宾
 */
@MapperScan("com.dingyabin.scheduler.mapper")
@SpringBootApplication
public class SchedulerSystemApplication {

    public static void main(String[] args) {
        //启动Spring容器
        new SpringApplicationBuilder(SchedulerSystemApplication.class).web(WebApplicationType.NONE).run(args);
    }

}
