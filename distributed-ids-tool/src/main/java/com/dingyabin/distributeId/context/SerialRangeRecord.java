package com.dingyabin.distributeId.context;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author Administrator
 * Date: 2024/10/1.
 * Time:21:51
 */
@Getter
@Setter
@Accessors(chain = true)
public class SerialRangeRecord {
    private double prefetchThreshold;

    private LinkedBlockingQueue<RangeRecord> rangeRecords = new LinkedBlockingQueue<>(2);

    public SerialRangeRecord(double prefetchThreshold) {
        this.prefetchThreshold = prefetchThreshold;
    }


    public void addRangeRecord(RangeRecord rangeRecord) {
        try {
            rangeRecords.put(rangeRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private int getRangeRecordSize() {
        return rangeRecords.size();
    }


    public boolean shouldPrefetch(){
        RangeRecord record = rangeRecords.peek();
        return record == null || ((getRangeRecordSize() == 1) && (record.getMaxId() - record.currentId()) < record.getStep() * prefetchThreshold);
    }


    public Long nextId() {
        for (RangeRecord rangeRecord : rangeRecords) {
            Long nextId = rangeRecord.nextId();
            //号段已经用完了，删除这个号段, 从下一个号段开始取
            if (nextId > rangeRecord.getMaxId()) {
                rangeRecords.remove(rangeRecord);
                continue;
            }
            return nextId;
        }
        //所有号段都不可用
        return null;
    }
}
