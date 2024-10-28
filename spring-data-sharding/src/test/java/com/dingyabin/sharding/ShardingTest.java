package com.dingyabin.sharding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.dingyabin.sharding.domain.Order;
import com.dingyabin.sharding.mapper.OrderMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/10/27.
 * Time:23:04
 */
@SpringBootTest
public class ShardingTest {

    @Resource
    private OrderMapper orderMapper;

    @Test
    public void testInsert() {
        Order order = new Order();
        order.setCusid(4L);
        order.setName("李2四1111");
        order.setCreateTime(new Date());
        orderMapper.insert(order);
    }


    @Test
    public void testBatchInsert() {
        List<Order> list = new ArrayList<>();
        for (int i = 7; i < 17; i++) {
            Order order = new Order();
            order.setCusid((long) i);
            order.setName("李四_" + i);
            order.setCreateTime(new Date());
            list.add(order);
        }
        orderMapper.insertBatch(list);
    }

}
