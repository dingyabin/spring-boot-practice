package com.dingyabin.distributeId.service.impl;

import com.dingyabin.distributeId.model.DistributeIdsRange;
import com.dingyabin.distributeId.service.DistributeIdsRangeService;
import com.dingyabin.distributeId.service.api.IDistributeId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:17:11
 */
@Service
public class DataBaseRangeDistributeId implements IDistributeId, InitializingBean {

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private Map<String, SerialRangeRecord> atomicLongMap = new ConcurrentHashMap<>();

    @Resource
    private DistributeIdsRangeService distributeIdsRangeService;


    @Override
    public void afterPropertiesSet() throws Exception {
        initMemoryIds();
    }


    @Override
    public Long nextId() {
        return nextId(DistributeIdsRangeService.DEFAULT_TYPE);
    }


    @Override
    public Long nextId(String bizType) {
        SerialRangeRecord serialRangeRecord = atomicLongMap.get(bizType);
        //号段用完了,需要刷新
        Long nextId = serialRangeRecord.nextId();
        if (nextId == null) {
            //刷新本地ids
            refreshRangeIds(bizType);
            serialRangeRecord = atomicLongMap.get(bizType);
            nextId = serialRangeRecord.nextId();
        }
        //如果超过阈值，则后台刷新
        if (serialRangeRecord.shouldPrefetch()) {
            //启动后台任务刷新本地ids
            executorService.submit(() -> {
                prefetchRangeIds(bizType);
            });
        }
        return nextId;
    }


    private void initMemoryIds() {
        List<DistributeIdsRange> totalDistributeIdsRanges = distributeIdsRangeService.getTotalDistributeIdsRanges();
        if (CollectionUtils.isEmpty(totalDistributeIdsRanges)) {
            return;
        }
        for (DistributeIdsRange idsRange : totalDistributeIdsRanges) {
            loadMemoryIds(idsRange.getBizType());
        }
    }


    private synchronized void refreshRangeIds(String bizType) {
        //DLC双重检查，防止别的线程刚刚已经刷新过了
        SerialRangeRecord curRangeRecord = atomicLongMap.get(bizType);
        if (curRangeRecord != null && curRangeRecord.nextId() != null) {
            return;
        }
        loadMemoryIds(bizType);
    }


    /**
     * 预取下一批id
     * @param bizType bizType
     */
    private synchronized void prefetchRangeIds(String bizType) {
        //防止别的线程已经预取过了
        SerialRangeRecord serialRangeRecord = atomicLongMap.get(bizType);
        if (!serialRangeRecord.shouldPrefetch()) {
            return;
        }
        loadMemoryIds(bizType);
    }



    private void loadMemoryIds(String bizType) {
        while (true) {
            DistributeIdsRange currentRange = distributeIdsRangeService.getRangeByBiz(bizType);
            int updateCount = distributeIdsRangeService.updateRangeStep(currentRange);
            if (updateCount == 0) {
                continue;
            }
            atomicLongMap.compute(bizType, (key, oldSerialRangeRecord) -> {
                RangeRecord rangeRecord = new RangeRecord(new AtomicLong(currentRange.getMaxId()), currentRange.getStep(), currentRange.getMaxId() + currentRange.getStep());
                if (oldSerialRangeRecord == null) {
                    SerialRangeRecord newSerialRangeRecord = new SerialRangeRecord();
                    newSerialRangeRecord.addRangeRecord(rangeRecord);
                    return newSerialRangeRecord;
                }
                oldSerialRangeRecord.addRangeRecord(rangeRecord);
                return oldSerialRangeRecord;
            });
            break;
        }
    }



    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    private static class RangeRecord {

        private AtomicLong startNotInclude;

        private Integer step;

        private Long maxId;

        public Long currentId(){
            return startNotInclude.get();
        }

        public Long nextId() {
            return startNotInclude.incrementAndGet();
        }

    }


    @Getter
    @Setter
    @Accessors(chain = true)
    private static class SerialRangeRecord {

        private List<RangeRecord> rangeRecords = new CopyOnWriteArrayList<>();


        public void addRangeRecord(RangeRecord rangeRecord) {
            rangeRecords.add(rangeRecord);
        }


        private int getRangeRecordSize() {
            return rangeRecords.size();
        }


        public boolean shouldPrefetch(){
            boolean onlyOneRangeRecord = getRangeRecordSize() == 1;
            RangeRecord record = rangeRecords.get(0);
            return onlyOneRangeRecord && (record.getMaxId() - record.currentId()) < record.getStep() * 0.3;
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

}
