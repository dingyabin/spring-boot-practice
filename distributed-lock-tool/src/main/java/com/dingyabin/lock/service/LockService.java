package com.dingyabin.lock.service;

import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.annotation.Lock4j;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 丁亚宾
 * Date: 2024/10/16.
 * Time:0:39
 */
@Service
public class LockService {

    @Resource
    private LockTemplate lockTemplate;


    @SneakyThrows
    @Lock4j(name = "AnnotationLock", keys = {"#id"}, acquireTimeout = 10)
    public void annotationLock(Long id) {
        Thread.sleep(10000);
        System.out.println("xxxxxxxxxxxxxxxx");
    }


    public void manualLock() {
        LockInfo lockInfo = lockTemplate.lock("manualLockTest", 10000, 100);
        if (lockInfo == null) {
            return;
        }
        try {
            Thread.sleep(10000);
            System.out.println("xxxxxxxxxxxxxxxx");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockTemplate.releaseLock(lockInfo);
        }
    }
}
