package com.dingyabin.prometheusdemo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:0:30
 */
@RestController
public class BizController {


    @RequestMapping("biz")
    public String biz() {
        return "ok";
    }

}
