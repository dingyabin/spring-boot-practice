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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:17:11
 */
@Service
public class DataBaseRangeDistributeId implements IDistributeId, InitializingBean {

    private final Object mutex = new Object();

    private final double PRE_FETCH_THRESHOLD = 0.4;

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
        if (serialRangeRecord == null) {
            throw new RuntimeException("暂不支持的业务类型!");
        }
        Long maxId;
        //内存里的号码段全部用完了,预取线程还没有刷新出来，需要马上刷新一批出来
        if ((maxId = serialRangeRecord.nextId()) == null) {
            preFetchMemoryIds(bizType);
            return serialRangeRecord.nextId();
        }
        //需要预取下一批次了
        if (serialRangeRecord.shouldPrefetch()) {
            executorService.submit(() -> {
                preFetchMemoryIds(bizType);
            });
        }
        return maxId;
    }


    /**
     * 初始化内存里的id号码段,只初始化一次
     */
    private void initMemoryIds() {
        List<DistributeIdsRange> totalDistributeIdsRanges = distributeIdsRangeService.getTotalDistributeIdsRanges();
        if (CollectionUtils.isEmpty(totalDistributeIdsRanges)) {
            return;
        }
        for (DistributeIdsRange idsRange : totalDistributeIdsRanges) {
            loadMemoryIds(idsRange);
        }
    }


    /**
     * 预取下一批号码段
     */
    private void preFetchMemoryIds(String bizType) {
        DistributeIdsRange currentRange = distributeIdsRangeService.getRangeByBiz(bizType);
        if (currentRange == null) {
            return;
        }
        synchronized (mutex) {
            SerialRangeRecord curRangeRecord = atomicLongMap.get(bizType);
            //只有需要刷新的号段业务才执行任务
            if (curRangeRecord.shouldPrefetch()) {
                loadMemoryIds(currentRange);
            }
        }
    }


    private void loadMemoryIds(DistributeIdsRange currentRange) {
        while (true) {
            int updateCount = distributeIdsRangeService.updateRangeStep(currentRange);
            //没有更新成功,重新拉取最新的
            if (updateCount == 0) {
                currentRange = distributeIdsRangeService.getRangeByBiz(currentRange.getBizType());
                continue;
            }
            Long maxId = currentRange.getMaxId();
            Integer step = currentRange.getStep();
            atomicLongMap.compute(currentRange.getBizType(), (key, oldSerialRangeRecord) -> {
                RangeRecord rangeRecord = new RangeRecord(new AtomicLong(maxId), step, maxId + step);
                if (oldSerialRangeRecord == null) {
                    SerialRangeRecord newSerialRangeRecord = new SerialRangeRecord(PRE_FETCH_THRESHOLD);
                    newSerialRangeRecord.offerRangeRecord(rangeRecord);
                    return newSerialRangeRecord;
                }
                oldSerialRangeRecord.offerRangeRecord(rangeRecord);
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

        private double prefetchThreshold;

        private LinkedBlockingQueue<RangeRecord> rangeRecords = new LinkedBlockingQueue<>(2);

        public SerialRangeRecord(double prefetchThreshold) {
            this.prefetchThreshold = prefetchThreshold;
        }

        public boolean offerRangeRecord(RangeRecord rangeRecord) {
           return rangeRecords.offer(rangeRecord);
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

}
