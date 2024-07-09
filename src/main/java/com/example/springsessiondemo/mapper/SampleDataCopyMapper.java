package com.example.springsessiondemo.mapper;

import com.example.springsessiondemo.entity.SampleDataCopy;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【sample_data_copy(测试表)】的数据库操作Mapper
* @createDate 2024-07-03 22:16:50
* @Entity com.example.springsessiondemo.entity.SampleDataCopy
*/
public interface SampleDataCopyMapper extends BaseMapper<SampleDataCopy> {


    int insertBatch(@Param("list") List<SampleDataCopy> list);

}




