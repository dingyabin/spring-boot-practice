package com.dingyabin.scheduler.config;

import com.dingyabin.scheduler.api.SimpleSchedulerTask;
import com.dingyabin.scheduler.model.DynamicTask;
import com.dingyabin.scheduler.service.impl.DynamicTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.scheduling.config.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author 丁亚宾
 * Date: 2024/11/12.
 * Time:22:48
 */
@Configuration
@ConditionalOnBean(value = ScheduledAnnotationBeanPostProcessor.class)
public class DynamicSchedulerTaskSwitchConfig {

    private final ScheduledAnnotationBeanPostProcessor postProcessor;


    private final ScheduledTaskRegistrar scheduledTaskRegistrar;


    private final Map<Task, ScheduledTask> unresolvedTasksInTaskRegistrar;


    private final DynamicTaskService dynamicTaskService;


    @Autowired(required = false)
    public DynamicSchedulerTaskSwitchConfig(ScheduledAnnotationBeanPostProcessor postProcessor, DynamicTaskService dynamicTaskService) {
        this.postProcessor = postProcessor;
        this.dynamicTaskService = dynamicTaskService;
        this.scheduledTaskRegistrar = findRegistrar();
        this.unresolvedTasksInTaskRegistrar = Collections.synchronizedMap(findUnresolvedTasksInRegistrar());
    }


    @Scheduled(fixedDelay = 2, timeUnit = TimeUnit.SECONDS)
    public void dynamicSchedulerSwitchTask() {
        Map<String, ScheduledTask> scheduledTaskMap = postProcessor.getScheduledTasks().stream()
                .filter(t -> t.getTask().getRunnable() instanceof SimpleSchedulerTask)
                .collect(Collectors.toMap(
                        t -> ((SimpleSchedulerTask) t.getTask().getRunnable()).getUniqueTaskName(),
                        t -> t)
                );
        if (CollectionUtils.isEmpty(scheduledTaskMap)) {
            return;
        }
        List<DynamicTask> dynamicTasks = dynamicTaskService.getDynamicTaskByNames(scheduledTaskMap.keySet());
        for (DynamicTask dynamicTask : dynamicTasks) {
            ScheduledTask scheduledTask = scheduledTaskMap.get(dynamicTask.getTaskName());
            //状态是停止，且之前是启动状态, 则本次需要取消运行
            if (dynamicTask.getStatus() == 0 && !unresolvedTasksInTaskRegistrar.containsKey(scheduledTask.getTask())) {
                cancelScheduledTask(scheduledTask);
            }
            if (dynamicTask.getStatus() == 1 && unresolvedTasksInTaskRegistrar.containsKey(scheduledTask.getTask())) {
                //重新启动
                reStartScheduledTask(scheduledTask.getTask());
            }
        }
    }


    /**
     * 用反射获取ScheduledAnnotationBeanPostProcessor里的ScheduledTaskRegistrar字段(因为没有提供get方法，只能反射)
     *
     * @return ScheduledTaskRegistrar
     */
    private ScheduledTaskRegistrar findRegistrar() {
        Field registrarField = ReflectionUtils.findField(ScheduledAnnotationBeanPostProcessor.class, "registrar");
        if (registrarField == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(registrarField);
        return (ScheduledTaskRegistrar) ReflectionUtils.getField(registrarField, postProcessor);
    }




    @SuppressWarnings("unchecked")
    private Map<Task, ScheduledTask> findUnresolvedTasksInRegistrar() {
        Field unresolvedTasksField = ReflectionUtils.findField(ScheduledTaskRegistrar.class, "unresolvedTasks");
        if (unresolvedTasksField == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(unresolvedTasksField);
        return (Map<Task, ScheduledTask>) ReflectionUtils.getField(unresolvedTasksField, scheduledTaskRegistrar);
    }



    private void cancelScheduledTask(ScheduledTask scheduledTask){
        scheduledTask.cancel(false);
        unresolvedTasksInTaskRegistrar.put(scheduledTask.getTask(), scheduledTask);
    }


    private void reStartScheduledTask(Task task) {
        if (task instanceof CronTask) {
            scheduledTaskRegistrar.scheduleCronTask((CronTask) task);
        }
        if (task instanceof TriggerTask) {
            scheduledTaskRegistrar.scheduleTriggerTask((TriggerTask) task);
        }
        if (task instanceof FixedDelayTask) {
            scheduledTaskRegistrar.scheduleFixedDelayTask((FixedDelayTask) task);
        }
        if (task instanceof FixedRateTask) {
            scheduledTaskRegistrar.scheduleFixedRateTask((FixedRateTask) task);
        }
        unresolvedTasksInTaskRegistrar.remove(task);
    }


}
