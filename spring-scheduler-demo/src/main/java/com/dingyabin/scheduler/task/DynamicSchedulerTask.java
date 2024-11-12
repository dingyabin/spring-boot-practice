package com.dingyabin.scheduler.task;

import com.dingyabin.scheduler.api.SimpleSchedulerTask;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

/**
 * @author 丁亚宾
 * Date: 2024/11/12.
 * Time:0:35
 */
@Component
public class DynamicSchedulerTask extends SimpleSchedulerTask {

    @Override
    public String getUniqueTaskName() {
        return "PRINT_DATA";
    }

    @Override
    public void run() {
        System.out.println("<<<<<<<<<<<<<<<<这是自定义的动态定时任务: " + Thread.currentThread().getName() + " " + LocalTime.now());
    }
}
