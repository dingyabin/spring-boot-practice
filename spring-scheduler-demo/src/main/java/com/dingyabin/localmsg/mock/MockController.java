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


    @RequestMapping("/test")
    public void localMessage() throws InterruptedException {
        mockService.mock("test", Arrays.asList("abc", "123"), new Student());
    }


    @RequestMapping("/test2")
    public void localMessage2() {
        mockService.poll();
    }


}
