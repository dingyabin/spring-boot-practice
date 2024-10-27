package com.dingyabin.sharding.mapper;

import com.dingyabin.sharding.domain.Order;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【order_0】的数据库操作Mapper
* @createDate 2024-10-27 23:09:05
* @Entity com.dingyabin.sharding.domain.Order
*/
public interface OrderMapper extends BaseMapper<Order> {


    int insertBatch(@Param("list") List<Order> list);

}




