package com.dingyabin.distributeId.controller;

import com.dingyabin.distributeId.service.impl.DataBaseRangeDistributeId;
import com.dingyabin.distributeId.service.impl.SnowflakeDistributeId;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

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

    @RequestMapping("/get")
    public Result<Long> snowflakeDistributeIdTest() {
        return Result.success(snowflakeDistributeId.nextId());
    }


    @RequestMapping("/get2")
    public Result<Long> dataBaseRangeDistributeIdTest() {
        return Result.success(dataBaseRangeDistributeId.nextId());
    }

    @RequestMapping("/get3")
    public Result<Integer> dataBaseRangeDistributeIdBathTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        Set<Object> objectSet = Collections.synchronizedSet(new HashSet<>());
        List<Future<?>> futures = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Future<?> submit = executorService.submit(() -> {
                for (int j = 0; j < 5000; j++) {
                    Long nextId = dataBaseRangeDistributeId.nextId();
                    if (nextId == null) {
                        System.out.println("--------出现空值，测试失败------------");
                    } else {
                        objectSet.add(nextId);
                    }
                }
            });
            futures.add(submit);
        }
        futures.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        System.out.println("total:" + objectSet.size());
        return Result.success(objectSet.size());
    }
}
