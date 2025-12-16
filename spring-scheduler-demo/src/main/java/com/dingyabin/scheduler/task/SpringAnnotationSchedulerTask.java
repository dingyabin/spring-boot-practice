package com.dingyabin.scheduler.task;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author 丁亚宾
 * Date: 2024/11/11.
 * Time:23:46
 */
@Component
public class SpringAnnotationSchedulerTask {

    @Scheduled(cron = "*/10 * * * * ?")
    @SchedulerLock(name = "springAnnotationSchedulerTask.execute", lockAtLeastFor = "5s", lockAtMostFor = "10s")
    public void execute(){
        System.out.println(">>>>>>>>>>>>>>这是Spring自带的@Scheduled注解的任务: "+ Thread.currentThread().getName()+ " " + LocalTime.now());
    }
}
