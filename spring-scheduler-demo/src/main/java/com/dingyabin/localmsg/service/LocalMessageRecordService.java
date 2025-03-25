package com.dingyabin.localmsg.service;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.localmsg.mapper.LocalMessageRecordMapper;
import com.dingyabin.localmsg.model.entity.LocalMessageRecord;
import com.dingyabin.localmsg.model.enums.LocalMsgStatusEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 丁亚宾
 * @description 针对表【local_message_record】的数据库操作Service实现
 * @createDate 2025-03-24 22:38:44
 */
@Service
public class LocalMessageRecordService extends ServiceImpl<LocalMessageRecordMapper, LocalMessageRecord> {


    public void saveLocalMessageRecord(LocalMessageRecord localMessageRecord) {
        localMessageRecord.setStatus(LocalMsgStatusEnum.INIT);
        save(localMessageRecord);
    }


    public void updateLocalMessageRecordFail(long id, String remark) {
        updateLocalMessage(id, LocalMsgStatusEnum.FAILED, remark);
    }


    public void updateLocalMessageRecordSuccess(long id) {
        updateLocalMessage(id, LocalMsgStatusEnum.SUCCESS, null);
    }


    public void updateLocalMessageRecordRetry(long id, String remark) {
        updateLocalMessage(id, LocalMsgStatusEnum.RETRY, remark);
    }


    public void updateLocalMessage(long id, LocalMsgStatusEnum localMsgStatusEnum, String remark) {
        LambdaUpdateWrapper<LocalMessageRecord> lambdaUpdate = Wrappers.lambdaUpdate();
        lambdaUpdate.set(LocalMessageRecord::getStatus, localMsgStatusEnum)
                .set(StringUtils.hasText(remark), LocalMessageRecord::getRemark, remark)
                .setSql("retry_time = retry_time + 1")
                .eq(LocalMessageRecord::getId, id);
        update(lambdaUpdate);
    }


    public List<LocalMessageRecord> findRetryLocalMessageRecord() {
        return baseMapper.selectRetryLocalMessage(10);
    }

}




