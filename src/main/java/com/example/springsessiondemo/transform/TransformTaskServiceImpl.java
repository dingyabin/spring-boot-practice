package com.example.springsessiondemo.transform;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springsessiondemo.entity.TransformTask;
import com.example.springsessiondemo.mapper.TransformTaskMapper;
import org.springframework.stereotype.Service;

/**
 * @author 丁亚宾
 * @description 针对表【transform_task】的数据库操作Service实现
 * @createDate 2024-07-03 22:16:50
 */
@Service("transformTaskService")
public class TransformTaskServiceImpl extends ServiceImpl<TransformTaskMapper, TransformTask> {


    public TransformTask newTransformTask(long startId, long size) {
        TransformTask transformTask = new TransformTask();
        transformTask.setStartId(startId);
        transformTask.setSize(size);
        transformTask.setStatus(0);
        save(transformTask);
        return transformTask;
    }


    public void finish(long id) {
        LambdaUpdateWrapper<TransformTask> updateWrapper = Wrappers.lambdaUpdate(TransformTask.class)
                .set(TransformTask::getStatus, 1)
                .eq(TransformTask::getId, id);
        baseMapper.update(updateWrapper);
    }

}




