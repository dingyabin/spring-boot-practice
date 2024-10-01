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
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:17:11
 */
@Service
public class DataBaseRangeDistributeId implements IDistributeId, InitializingBean {

    private final Object mutex = new Object();

    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private Map<String, SerialRangeRecord> atomicLongMap = new ConcurrentHashMap<>();

    @Resource
    private DistributeIdsRangeService distributeIdsRangeService;


    @Override
    public void afterPropertiesSet() throws Exception {
        executorService.submit(()-> {
            try {
                initMemoryIds();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    @Override
    public Long nextId() {
        return nextId(DistributeIdsRangeService.DEFAULT_TYPE);
    }


    @Override
    public Long nextId(String bizType) {
        SerialRangeRecord serialRangeRecord = atomicLongMap.get(bizType);
        Long maxId = null;
        if (serialRangeRecord == null || (maxId = serialRangeRecord.nextId()) == null || serialRangeRecord.shouldPrefetch()) {
            synchronized(mutex) {
                mutex.notifyAll();
            }

        }
        return maxId;
    }


    private void initMemoryIds() throws InterruptedException {
        while (true) {
            List<DistributeIdsRange> totalDistributeIdsRanges = distributeIdsRangeService.getTotalDistributeIdsRanges();
            if (CollectionUtils.isEmpty(totalDistributeIdsRanges)) {
                return;
            }
            synchronized (mutex) {
                for (DistributeIdsRange idsRange : totalDistributeIdsRanges) {
                    SerialRangeRecord curRangeRecord = atomicLongMap.get(idsRange.getBizType());
                    //只有需要刷新的号段业务才执行任务
                    if (curRangeRecord == null || curRangeRecord.shouldPrefetch()) {
                        loadMemoryIds(idsRange);
                    }
                }
                //执行完一次任务后，休息
                mutex.wait();
            }
        }
    }


//    private synchronized void refreshRangeIds(String bizType) {
//        //DLC双重检查，防止别的线程刚刚已经刷新过了
//        SerialRangeRecord curRangeRecord = atomicLongMap.get(bizType);
//        if (curRangeRecord != null && curRangeRecord.nextId() != null) {
//            return;
//        }
//        loadMemoryIds(bizType);
//    }
//

//    /**
//     * 预取下一批id
//     * @param bizType bizType
//     */
//    private synchronized void prefetchRangeIds(String bizType) {
//        //防止别的线程已经预取过了
//        SerialRangeRecord serialRangeRecord = atomicLongMap.get(bizType);
//        if (!serialRangeRecord.shouldPrefetch()) {
//            return;
//        }
//        loadMemoryIds(bizType);
//    }


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
                    SerialRangeRecord newSerialRangeRecord = new SerialRangeRecord();
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

        private LinkedBlockingQueue<RangeRecord> rangeRecords = new LinkedBlockingQueue<>(2);

        public boolean offerRangeRecord(RangeRecord rangeRecord) {
           return rangeRecords.offer(rangeRecord);
        }

        private int getRangeRecordSize() {
            return rangeRecords.size();
        }


        public boolean shouldPrefetch(){
            RangeRecord record = rangeRecords.peek();
            return record == null || ((getRangeRecordSize() == 1) && (record.getMaxId() - record.currentId()) < record.getStep() * 0.3);
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
