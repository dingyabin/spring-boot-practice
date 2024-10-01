package com.dingyabin.distributeId.controller;

import com.dingyabin.distributeId.service.impl.DataBaseRangeDistributeId;
import com.dingyabin.distributeId.service.impl.SnowflakeDistributeId;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:21:31
 */
@RestController
public class IdController {

    @Resource
    private SnowflakeDistributeId snowflakeDistributeId;

    @Resource
    private DataBaseRangeDistributeId dataBaseRangeDistributeId;

    @GetMapping("/get")
    public Result<Long> snowflakeDistributeIdTest() {
        return Result.success(snowflakeDistributeId.nextId());
    }


    @GetMapping("/get2")
    public Result<Long> dataBaseRangeDistributeIdTest() {
        return Result.success(dataBaseRangeDistributeId.nextId());
    }

    @GetMapping("/get3")
    public Result<Long> dataBaseRangeDistributeIdBathTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Set<Object> objectSet = Collections.synchronizedSet(new HashSet<>());
        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 5000; j++) {
                    Long nextId = dataBaseRangeDistributeId.nextId();
                    objectSet.add(nextId);
                    if (nextId == null) {
                        System.out.println("--------出现空值，测试失败------------");
                    }
                }
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("total:" + objectSet.size());
        return Result.success();
    }
}
