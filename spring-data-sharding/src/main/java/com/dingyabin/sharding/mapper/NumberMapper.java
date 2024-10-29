package com.dingyabin.sharding.mapper;

import com.dingyabin.sharding.domain.Number;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【number_0】的数据库操作Mapper
* @createDate 2024-10-29 18:46:00
* @Entity com.dingyabin.sharding.domain.Number
*/
public interface NumberMapper extends BaseMapper<Number> {


    int insertBatch(@Param("list") List<Number> list);

}




