package com.dingyabin.scheduler.config;

import com.dingyabin.scheduler.api.SimpleSchedulerTask;
import com.dingyabin.scheduler.model.DynamicTask;
import com.dingyabin.scheduler.service.impl.DynamicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.task.TaskSchedulingAutoConfiguration;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author 丁亚宾
 * Date: 2024/11/11.
 * Time:23:50
 */
@Configuration
@EnableScheduling
public class DynamicSchedulerTaskConfig implements SchedulingConfigurer {

    /**
     * 系统中自定义的定时任务类
     */
    private List<SimpleSchedulerTask> simpleSchedulerTasks;

    @Resource
    private DynamicTaskService dynamicTaskService;


    private Map<String, CronTrigger> TRIGGER_MAP;


    @Autowired(required = false)
    public DynamicSchedulerTaskConfig(List<SimpleSchedulerTask> simpleSchedulerTasks) {
        if (simpleSchedulerTasks != null) {
            this.simpleSchedulerTasks = simpleSchedulerTasks;
            TRIGGER_MAP = new ConcurrentHashMap<>(simpleSchedulerTasks.size());
        }
    }


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        if (simpleSchedulerTasks == null) {
            return;
        }
        for (SimpleSchedulerTask simpleSchedulerTask : simpleSchedulerTasks) {
            taskRegistrar.addTriggerTask(simpleSchedulerTask::execute, triggerContext -> {
                String uniqueTaskName = simpleSchedulerTask.getUniqueTaskName();
                DynamicTask dynamicTask = dynamicTaskService.getDynamicTaskByName(uniqueTaskName);
                if (dynamicTask == null) {
                    throw new RuntimeException("找不到name=" + uniqueTaskName + "的任务!");
                }
                String cron = dynamicTask.getCron();
                System.out.printf("----------%s的cron表达式为%s -----------\r\n", uniqueTaskName, cron);
                CronTrigger cronTrigger = TRIGGER_MAP.computeIfAbsent(cron, s -> new CronTrigger(cron));
                return cronTrigger.nextExecutionTime(triggerContext);
            });
        }
    }


    /**
     *
     * 如果没有 SchedulingConfigurer、TaskScheduler、 ScheduledExecutorService 这几个bean 那么spring
     * 会默认注册一个ThreadPoolTaskScheduler(用到配置文件里的配置)，现在自定义了一个SchedulingConfigurer, 默认注册会失效
     * 需要自己注册一个ThreadPoolTaskScheduler的bean, 可以选择使用配置文件里的配置(TaskSchedulerBuilder)
     *
     * @see TaskSchedulingAutoConfiguration
     * @see ScheduledAnnotationBeanPostProcessor#finishRegistration
     *
     * @param builder
     * @return ThreadPoolTaskScheduler
     */
    @Bean
    public ThreadPoolTaskScheduler taskScheduler(TaskSchedulerBuilder builder) {
        return builder.build();
    }
}
