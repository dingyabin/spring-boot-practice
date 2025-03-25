package com.dingyabin.localmsg.mock;

import com.alibaba.fastjson2.JSON;
import com.dingyabin.localmsg.annotation.LocalMessage;
import com.dingyabin.localmsg.model.entity.LocalMessageRecord;
import com.dingyabin.localmsg.service.InvokeService;
import com.dingyabin.localmsg.service.LocalMessageRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MockService {

    @Resource
    private InvokeService invokeService;

    @Resource
    private LocalMessageRecordService localMessageRecordService;


    @LocalMessage(bizType = "TEST", maxRetryTime = 3, sync = false)
    public Integer mock(String str, List<String> list, Student student) throws InterruptedException {
        System.out.printf("%s----%s-----%s", str, JSON.toJSONString(list), JSON.toJSONString(student));
        Thread.sleep(2000L);
        return 1;
    }


    public void poll() {
        List<LocalMessageRecord> retryLocalMessageRecord = localMessageRecordService.findRetryLocalMessageRecord();
        if (retryLocalMessageRecord == null) {
            return;
        }
        for (LocalMessageRecord localMessageRecord : retryLocalMessageRecord) {
            invokeService.doInvoke(localMessageRecord, false);
        }
    }

}
