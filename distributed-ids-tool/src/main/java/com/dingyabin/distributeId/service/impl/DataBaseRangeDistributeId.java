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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:17:11
 */
@Service
public class DataBaseRangeDistributeId implements IDistributeId, InitializingBean {

    private final double refreshThreshold = 0.7;

    private ReentrantLock lock = new ReentrantLock();

    private Map<String, RangeRecord> atomicLongMap = new ConcurrentHashMap<>();

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
        RangeRecord rangeRecord = atomicLongMap.get(bizType);
        Long nextId = rangeRecord.nextId();
        if (nextId > rangeRecord.getMaxId()) {
            throw new RuntimeException("异常,请稍后再试!");
        }
        //如果超过阈值，则后台刷新
        if (nextId >  refreshThreshold * rangeRecord.getMaxId() ) {
            refreshRangeIds(bizType);
        }
        return nextId;
    }


    private void initMemoryIds() {
        List<DistributeIdsRange> totalDistributeIdsRanges = distributeIdsRangeService.getTotalDistributeIdsRanges();
        if (CollectionUtils.isEmpty(totalDistributeIdsRanges)) {
            return;
        }
        for (DistributeIdsRange idsRange : totalDistributeIdsRanges) {
            refreshRangeIds(idsRange.getBizType());
        }
    }


    private void refreshRangeIds(String bizType) {
        //先尝试获取锁, 失败的话意味着有线程正在执行刷新任务
        if (!lock.tryLock()) {
            return;
        }
        try {
            while (true) {
                DistributeIdsRange currentRange = distributeIdsRangeService.getRangeByBiz(bizType);
                int updateCount = distributeIdsRangeService.updateRangeStep(currentRange);
                if (updateCount == 0) {
                    continue;
                }
                RangeRecord rangeRecord = new RangeRecord(new AtomicLong(currentRange.getMaxId()), currentRange.getMaxId() + currentRange.getStep());
                atomicLongMap.put(currentRange.getBizType(), rangeRecord);
//
//                atomicLongMap.compute(bizType, (key, rangeRecord) -> {
//                    if (rangeRecord == null){
//                        return new RangeRecord(new AtomicLong(currentRange.getMaxId()), currentRange.getMaxId() + currentRange.getStep());
//                    }
//                    rangeRecord.setMaxId(  );
//                    return rangeRecord;
//                });
                break;
            }
        } finally {
            lock.unlock();
        }
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    private static class RangeRecord {

        private AtomicLong startNotInclude;

        private Long maxId;

        public Long nextId() {
            return startNotInclude.incrementAndGet();
        }

    }

}
