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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:17:11
 */
@Service
public class DataBaseRangeDistributeId implements IDistributeId, InitializingBean {

    private final double refreshThreshold = 0.3;

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
        if (rangeRecord.nextId() > rangeRecord.getMaxId()) {
            //刷新本地ids
            refreshRangeIds(bizType);
            rangeRecord = atomicLongMap.get(bizType);
        }
        Long nextId = rangeRecord.nextId();
        //如果超过阈值，则后台刷新
        if ((rangeRecord.getMaxId() - nextId) < rangeRecord.getStep() * refreshThreshold) {
            //启动后台任务刷新本地ids

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




    private synchronized void refreshRangeIds(String bizType) {
        //DLC双重检查，防止别的线程刚刚已经刷新过了
        RangeRecord curRangeRecord = atomicLongMap.get(bizType);
        if (curRangeRecord != null && curRangeRecord.nextId() < curRangeRecord.getMaxId()) {
            return;
        }
        while (true) {
            DistributeIdsRange currentRange = distributeIdsRangeService.getRangeByBiz(bizType);
            int updateCount = distributeIdsRangeService.updateRangeStep(currentRange);
            if (updateCount == 0) {
                continue;
            }
            RangeRecord newRangeRecord = new RangeRecord(new AtomicLong(currentRange.getMaxId()), currentRange.getStep(), currentRange.getMaxId() + currentRange.getStep());
            atomicLongMap.put(currentRange.getBizType(), newRangeRecord);
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
    }





    @Getter
    @Setter
    @Accessors(chain = true)
    @AllArgsConstructor
    private static class RangeRecord {

        private AtomicLong startNotInclude;

        private Integer step;

        private Long maxId;

        public Long nextId() {
            return startNotInclude.incrementAndGet();
        }

    }


    @Getter
    @Setter
    @Accessors(chain = true)
    private static class SerialRangeRecord {

        private List<RangeRecord>  rangeRecords = new CopyOnWriteArrayList<>();


        public void addRangeRecord(RangeRecord rangeRecord){
            rangeRecords.add(rangeRecord);
        }

        public Long nextId() {
            Iterator<RangeRecord> iterator = rangeRecords.iterator();
            while (iterator.hasNext()) {
                RangeRecord rangeRecord = iterator.next();
                Long nextId = rangeRecord.nextId();
                if (nextId > rangeRecord.getMaxId()) {
                    iterator.remove();
                    continue;
                }
                return nextId;
            }
            return null;
        }
    }

}
