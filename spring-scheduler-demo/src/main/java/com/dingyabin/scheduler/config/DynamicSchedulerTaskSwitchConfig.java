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
import java.util.concurrent.ConcurrentHashMap;
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


    private final Set<ScheduledTask> scheduledTasksInTaskRegistrar;


    private final DynamicTaskService dynamicTaskService;


    private final Map<String, ScheduledTask> STOPED_TASK = new ConcurrentHashMap<>();


    @Autowired(required = false)
    public DynamicSchedulerTaskSwitchConfig(ScheduledAnnotationBeanPostProcessor postProcessor, DynamicTaskService dynamicTaskService) {
        this.postProcessor = postProcessor;
        this.dynamicTaskService = dynamicTaskService;
        this.scheduledTaskRegistrar = findRegistrar();
        this.unresolvedTasksInTaskRegistrar = Collections.synchronizedMap(findUnresolvedTasksInRegistrar());
        this.scheduledTasksInTaskRegistrar = Collections.synchronizedSet(findScheduledTasksInTaskRegistrar());
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
            //状态是停止，且之前是启动状态, 则本次需要取消运行
            if (dynamicTask.getStatus() == 0 && !STOPED_TASK.containsKey(dynamicTask.getTaskName())) {
                ScheduledTask scheduledTask = scheduledTaskMap.get(dynamicTask.getTaskName());
                cancelScheduledTask(scheduledTask, dynamicTask.getTaskName());
            }
            if (dynamicTask.getStatus() == 1 && STOPED_TASK.containsKey(dynamicTask.getTaskName())) {
                //重新启动
                ScheduledTask scheduledTask = scheduledTaskMap.get(dynamicTask.getTaskName());
                reStartScheduledTask(scheduledTask.getTask(), dynamicTask.getTaskName());
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



    @SuppressWarnings("unchecked")
    private Set<ScheduledTask> findScheduledTasksInTaskRegistrar() {
        Field scheduledTasksField = ReflectionUtils.findField(ScheduledTaskRegistrar.class, "scheduledTasks");
        if (scheduledTasksField == null) {
            return null;
        }
        ReflectionUtils.makeAccessible(scheduledTasksField);
        return (Set<ScheduledTask>) ReflectionUtils.getField(scheduledTasksField, scheduledTaskRegistrar);
    }


    private void cancelScheduledTask(ScheduledTask scheduledTask, String taskName){
        scheduledTask.cancel(false);
        unresolvedTasksInTaskRegistrar.put(scheduledTask.getTask(), scheduledTask);
//        STOPED_TASK.put(taskName, scheduledTask);
        scheduledTasksInTaskRegistrar.remove(scheduledTask);
    }


    private void reStartScheduledTask(Task task, String taskName) {
        ScheduledTask scheduledTask = null;
        if (task instanceof CronTask) {
            scheduledTask = scheduledTaskRegistrar.scheduleCronTask((CronTask) task);
        }
        if (task instanceof TriggerTask) {
            scheduledTask = scheduledTaskRegistrar.scheduleTriggerTask((TriggerTask) task);
        }
        if (task instanceof FixedDelayTask) {
            scheduledTask = scheduledTaskRegistrar.scheduleFixedDelayTask((FixedDelayTask) task);
        }
        if (task instanceof FixedRateTask) {
            scheduledTask = scheduledTaskRegistrar.scheduleFixedRateTask((FixedRateTask) task);
        }
        unresolvedTasksInTaskRegistrar.remove(task);
        scheduledTasksInTaskRegistrar.add(scheduledTask);
//        STOPED_TASK.remove(taskName);
    }


}
