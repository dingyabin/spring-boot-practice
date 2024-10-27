package com.dingyabin.sharding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import com.dingyabin.sharding.domain.Order;
import com.dingyabin.sharding.service.impl.OrderService;

@Slf4j
@RestController
@RequestMapping("/Order")
public class OrderController {


    @Resource
    private OrderService  orderService;


    @PostMapping("/add")
    public Object addOrder(@RequestBody Order addObject) {
        orderService.save(addObject);
        return "ok";
    }


    @PostMapping("/del")
    public Object delOrder(@RequestBody Order delObject) {
         orderService.removeById(delObject);
         return "ok";
    }


    @PostMapping("/update")
    public Object updateOrder(@RequestBody Order updateObject) {
         orderService.updateById(updateObject);
         return "ok";
    }


    @PostMapping("/query")
    public List<Order> queryOrder(@RequestBody Order queryObject) {
         return new ArrayList<>();
    }

}