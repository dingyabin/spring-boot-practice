package com.dingyabin.sharding;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.dingyabin.sharding.domain.Customer;
import com.dingyabin.sharding.domain.Number;
import com.dingyabin.sharding.domain.Order;
import com.dingyabin.sharding.mapper.CustomerMapper;
import com.dingyabin.sharding.mapper.NumberMapper;
import com.dingyabin.sharding.mapper.OrderMapper;
import com.dingyabin.sharding.service.impl.OrderService;
import groovy.lang.Closure;
import groovy.util.Expando;
import org.apache.shardingsphere.underlying.common.config.inline.InlineExpressionParser;
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

    @Resource
    private CustomerMapper customerMapper;

    @Resource
    private NumberMapper numberMapper;

    @Resource
    private OrderService orderService;

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
        for (int i = 7; i < 18; i++) {
            Order order = new Order();
            order.setCusid((long) i);
            order.setName("张三_" + i);
            order.setCreateTime(new Date());
            list.add(order);
        }
        orderMapper.insertBatch(list);
    }



    @Test
    public void testTransactional(){
        orderService.transactionalSave(true);
    }


    @Test
    public void testCustomer(){
        List<Customer> customers = customerMapper.selectList(Wrappers.emptyWrapper());
        System.out.println(customers);
    }


    @Test
    public void testCustomerSharding() {
        for (int i = 0; i < 5; i++) {
            Number number = new Number();
            number.setNum(new Random().nextInt(1000000));
            number.setCreateTime(new Date());
            numberMapper.insert(number);
        }
        LambdaQueryWrapper<Number> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.ge(Number::getNum, 325134);
        lambdaQuery.le(Number::getNum, 325134);
        List<Number> numbers = numberMapper.selectList(lambdaQuery);
        System.out.println(numbers);
    }


    @Test
    public void testGroovy(){
        String value = "31235ertert123L";
        Closure<?>  closure = new InlineExpressionParser("${value.hashCode()}").evaluateClosure();
        Closure<?> result = closure.rehydrate(new Expando(), null, null);
        result.setResolveStrategy(Closure.DELEGATE_ONLY);
        result.setProperty("value", value);
        System.out.println(result.call().toString());
        System.out.println(value.hashCode());
    }

}
