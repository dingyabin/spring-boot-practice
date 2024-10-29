package com.dingyabin.sharding.controller;

import com.dingyabin.sharding.domain.Number;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.ArrayList;

import com.dingyabin.sharding.service.impl.NumberService;

@Slf4j
@RestController
@RequestMapping("/Number0")
public class Number0Controller {


    @Resource
    private NumberService number0Service;


    @PostMapping("/add")
    public Object addNumber0(@RequestBody Number addObject) {
        number0Service.save(addObject);
        return "ok";
    }


    @PostMapping("/del")
    public Object delNumber0(@RequestBody Number delObject) {
         number0Service.removeById(delObject);
         return "ok";
    }


    @PostMapping("/update")
    public Object updateNumber0(@RequestBody Number updateObject) {
         number0Service.updateById(updateObject);
         return "ok";
    }


    @PostMapping("/query")
    public List<Number> queryNumber0(@RequestBody Number queryObject) {
         return new ArrayList<>();
    }

}