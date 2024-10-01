package com.dingyabin.distributeId.context;

import com.dingyabin.distributeId.model.DistributeIdsRange;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 * Date: 2024/10/1.
 * Time:21:05
 */
@Getter
@Setter
@Accessors(fluent = true)
public class MemoryIdsRefreshContext {

   private RangeRecord  rangeRecord;

   private DistributeIdsRange currentRange;

    private Long nextId;

    public static MemoryIdsRefreshContext instance(){
        return new MemoryIdsRefreshContext();
    }

}
