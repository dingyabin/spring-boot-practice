package com.dingyabin.localmsg.mock;

import com.alibaba.fastjson2.JSON;
import com.dingyabin.localmsg.annotation.LocalMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MockService {


    @LocalMessage(bizType = "TEST", maxRetryTime = 3)
    public Integer mock(String str, List<String> list, Student student) {
        System.out.printf("%s----%s-----%s", str, JSON.toJSONString(list), JSON.toJSONString(student));
        return 1;
    }

}
