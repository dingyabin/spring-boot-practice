package com.dingyabin.distributeId.controller;

import com.dingyabin.distributeId.service.api.impl.DataBaseRangeDistributeId;
import com.dingyabin.distributeId.service.api.impl.LeafAllocDistributeId;
import com.dingyabin.distributeId.service.api.impl.SnowflakeDistributeId;
import com.dingyabin.response.Result;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @Resource
    private LeafAllocDistributeId leafAllocDistributeId;

    @RequestMapping("/get")
    public Result<Long> snowflakeDistributeIdTest() {
        return Result.success(snowflakeDistributeId.nextId());
    }


    @RequestMapping("/get2")
    public Result<Long> dataBaseRangeDistributeIdTest() {
        return Result.success(dataBaseRangeDistributeId.nextId());
    }


    @RequestMapping("/get5")
    public Result<Long> leafAllocDistributeIdTest() {
        return Result.success(leafAllocDistributeId.nextId());
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


    @RequestMapping("/get4")
    public Result<Map<String, String>> get4(@RequestBody Map<String, String> map) {
        return Result.success(map);
    }
}
