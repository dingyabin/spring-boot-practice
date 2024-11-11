package com.dingyabin.scheduler.mapper;

import com.dingyabin.scheduler.model.DynamicTask;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【dynamic_task】的数据库操作Mapper
* @createDate 2024-11-12 00:10:04
* @Entity com.dingyabin.scheduler.model.DynamicTask
*/
public interface DynamicTaskMapper extends BaseMapper<DynamicTask> {


    int insertBatch(@Param("list") List<DynamicTask> list);

}




