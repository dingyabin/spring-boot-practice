package com.dingyabin.sharding.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.sharding.domain.Order;
import com.dingyabin.sharding.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author 丁亚宾
 * @description 针对表【order_0】的数据库操作Service实现
 * @createDate 2024-10-27 23:09:05
 */
@Service
public class OrderService extends ServiceImpl<OrderMapper, Order> {


    @Transactional(rollbackFor = Exception.class)
    public void transactionalSave(boolean ex) {
        Order order1 = new Order();
        order1.setCusid((long) 11);
        order1.setName("王五11");
        order1.setCreateTime(new Date());
        save(order1);

        Order order2 = new Order();
        order2.setCusid((long) 12);
        order2.setName("王五12");
        order2.setCreateTime(new Date());
        save(order2);

        if (ex) {
            throw new RuntimeException("异常了...");
        }
    }

}




