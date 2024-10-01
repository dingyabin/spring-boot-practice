package com.dingyabin.distributeId.context;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Administrator
 * Date: 2024/10/1.
 * Time:21:50
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
public class RangeRecord {

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
