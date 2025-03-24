package com.dingyabin.localmsg.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.localmsg.entity.LocalMessageRecord;
import com.dingyabin.localmsg.mapper.LocalMessageRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author 丁亚宾
 * @description 针对表【local_message_record】的数据库操作Service实现
 * @createDate 2025-03-24 22:38:44
 */
@Service
public class LocalMessageRecordService extends ServiceImpl<LocalMessageRecordMapper, LocalMessageRecord> {


    public void saveLocalMessageRecord(LocalMessageRecord localMessageRecord) {
        localMessageRecord.setStatus(1);
        save(localMessageRecord);
    }

}




