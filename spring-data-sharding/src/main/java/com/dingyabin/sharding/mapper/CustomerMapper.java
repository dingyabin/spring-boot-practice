package com.dingyabin.sharding.mapper;

import com.dingyabin.sharding.domain.Customer;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
* @author 丁亚宾
* @description 针对表【customer】的数据库操作Mapper
* @createDate 2024-10-29 16:41:11
* @Entity com.dingyabin.sharding.domain.Customer
*/
public interface CustomerMapper extends BaseMapper<Customer> {


    int insertBatch(@Param("list") List<Customer> list);

}




