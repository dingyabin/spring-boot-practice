package com.dingyabin.distributeId.service.impl;

import com.dingyabin.distributeId.context.MemoryIdsRefreshContext;
import com.dingyabin.distributeId.context.RangeRecord;
import com.dingyabin.distributeId.context.SerialRangeRecord;
import com.dingyabin.distributeId.model.DistributeIdsRange;
import com.dingyabin.distributeId.service.DistributeIdsRangeService;
import com.dingyabin.distributeId.service.api.IDistributeId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

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
            maxId = fetchMemoryIdsAndGet(bizType);
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
            loadMemoryIds(idsRange, null);
        }
    }


    /**
     * 预取下一批号码段
     */
    private void preFetchMemoryIds(String bizType) {
        synchronized (mutex) {
            SerialRangeRecord curRangeRecord = atomicLongMap.get(bizType);
            //DLC双重检查，只有需要刷新的号段业务才执行任务
            if (curRangeRecord.shouldPrefetch()) {
                DistributeIdsRange currentRange = distributeIdsRangeService.getRangeByBiz(bizType);
                loadMemoryIds(currentRange, null);
            }
        }
    }

    /**
     * 预取下一批号码段,并且返回一个可用的id
     */
    private Long fetchMemoryIdsAndGet(String bizType) {
        synchronized (mutex) {
            SerialRangeRecord curRangeRecord = atomicLongMap.get(bizType);
            Long nextId = curRangeRecord.nextId();
            if (nextId != null) {
                return nextId;
            }
            DistributeIdsRange currentRange = distributeIdsRangeService.getRangeByBiz(bizType);
            MemoryIdsRefreshContext context = MemoryIdsRefreshContext.instance();
            loadMemoryIds(currentRange, refreshContext -> {
                context.nextId(refreshContext.rangeRecord().nextId());
            });
            return context.nextId();
        }
    }


    private void loadMemoryIds(DistributeIdsRange currentRange, Consumer<MemoryIdsRefreshContext> callback) {
        while (true) {
            int updateCount = distributeIdsRangeService.updateRangeStep(currentRange);
            //没有更新成功,重新拉取最新的
            if (updateCount == 0) {
                currentRange = distributeIdsRangeService.getRangeByBiz(currentRange.getBizType());
                continue;
            }
            RangeRecord rangeRecord = new RangeRecord(new AtomicLong(currentRange.getMaxId()), currentRange.getStep(), currentRange.getMaxId() + currentRange.getStep());
           //回调函数不为空，则执行回调函数
            if (callback != null) {
                MemoryIdsRefreshContext refreshContext = MemoryIdsRefreshContext.instance().rangeRecord(rangeRecord).currentRange(currentRange);
                callback.accept(refreshContext);
            }
            //把从数据库加载到的号码段放入内存
            atomicLongMap.compute(currentRange.getBizType(), (key, oldSerialRangeRecord) -> {
                if (oldSerialRangeRecord == null) {
                    SerialRangeRecord newSerialRangeRecord = new SerialRangeRecord(PRE_FETCH_THRESHOLD);
                    newSerialRangeRecord.addRangeRecord(rangeRecord);
                    return newSerialRangeRecord;
                }
                oldSerialRangeRecord.addRangeRecord(rangeRecord);
                return oldSerialRangeRecord;
            });
            break;
        }
    }

}
