package com.dingyabin.scheduler.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.scheduler.model.DynamicTask;
import com.dingyabin.scheduler.mapper.DynamicTaskMapper;
import org.springframework.stereotype.Service;

/**
 * @author 丁亚宾
 * @description 针对表【dynamic_task】的数据库操作Service实现
 * @createDate 2024-11-12 00:10:04
 */
@Service
public class DynamicTaskService extends ServiceImpl<DynamicTaskMapper, DynamicTask> {


    public DynamicTask getDynamicTaskByName(String name) {
        LambdaQueryWrapper<DynamicTask> wrapper = Wrappers.lambdaQuery(DynamicTask.class).eq(DynamicTask::getTaskName, name);
        return getOne(wrapper);
    }

}




