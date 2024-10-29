package com.dingyabin.sharding.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.ArrayList;
import com.dingyabin.sharding.domain.Customer;
import com.dingyabin.sharding.service.impl.CustomerService;

@Slf4j
@RestController
@RequestMapping("/Customer")
public class CustomerController {


    @Resource
    private CustomerService  customerService;


    @PostMapping("/add")
    public Object addCustomer(@RequestBody Customer addObject) {
        customerService.save(addObject);
        return "ok";
    }


    @PostMapping("/del")
    public Object delCustomer(@RequestBody Customer delObject) {
         customerService.removeById(delObject);
         return "ok";
    }


    @PostMapping("/update")
    public Object updateCustomer(@RequestBody Customer updateObject) {
         customerService.updateById(updateObject);
         return "ok";
    }


    @PostMapping("/query")
    public List<Customer> queryCustomer(@RequestBody Customer queryObject) {
         return new ArrayList<>();
    }

}