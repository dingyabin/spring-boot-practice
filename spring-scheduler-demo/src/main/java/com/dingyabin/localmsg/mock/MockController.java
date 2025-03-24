package com.dingyabin.localmsg.mock;

import com.dingyabin.localmsg.service.InvokeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Arrays;

@RestController
public class MockController {

    @Resource
    private MockService mockService;

    @Resource
    private InvokeService invokeService;


    @RequestMapping("/test")
    public void localMessage() {
        mockService.mock("test", Arrays.asList("abc", "123"), new Student());
    }


    @RequestMapping("/test2")
    public void localMessage2() {
        invokeService.test();
    }


}
