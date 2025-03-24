package com.dingyabin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


/**
 * @author 丁亚宾
 */
@EnableAspectJAutoProxy
@MapperScan(basePackages = {"com.dingyabin.scheduler.mapper", "com.dingyabin.localmsg.mapper"})
@SpringBootApplication
public class SchedulerSystemApplication {

    public static void main(String[] args) {
//        new SpringApplicationBuilder(SchedulerSystemApplication.class).web(WebApplicationType.NONE).run(args);
        SpringApplication.run(SchedulerSystemApplication.class, args);
    }

}
