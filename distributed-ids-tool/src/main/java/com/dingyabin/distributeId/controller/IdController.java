package com.dingyabin.distributeId.controller;

import com.dingyabin.distributeId.service.impl.DataBaseRangeDistributeId;
import com.dingyabin.distributeId.service.impl.SnowflakeDistributeId;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:21:31
 */
@RestController
public class IdController {

    ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Resource
    private SnowflakeDistributeId snowflakeDistributeId;

    @Resource
    private DataBaseRangeDistributeId dataBaseRangeDistributeId;

    @GetMapping("/get")
    public Result<Long> idTest() {
        return Result.success(snowflakeDistributeId.nextId());
    }


    @GetMapping("/get2")
    public Result<Long> idTest2() {
        return Result.success(dataBaseRangeDistributeId.nextId());
    }

    @GetMapping("/get3")
    public Result<Long> idTest3() {
        for (int i = 0; i < 50000; i++) {
            executorService.submit(()->{
                Long nextId = dataBaseRangeDistributeId.nextId();
                //objects.add(nextId);
                if (nextId == null) {
                    System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                }
            });
        }
        return Result.success("dataBaseRangeDistributeId.nextId()");
    }
}
