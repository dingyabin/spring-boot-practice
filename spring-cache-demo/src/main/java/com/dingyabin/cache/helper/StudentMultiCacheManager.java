package com.dingyabin.cache.helper;

import com.dingyabin.cache.helper.api.MultiCacheManager;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/10/29.
 * Time:22:17
 */
public class StudentMultiCacheManager extends MultiCacheManager<String, String> {


    @Override
    protected Caffeine<Object, Object> customizers(Caffeine<Object, Object> kvCaffeine) {
        kvCaffeine.maximumSize(500)
                .expireAfterWrite(10, TimeUnit.SECONDS);
        return kvCaffeine;
    }

    @Override
    protected void init() {
        super.timeOut = 120;
    }

    @Override
    protected String loadObjectInRepository(String key) {
        System.out.println("new了一个新的Student-" + key);
        return "新的Student-" + key;
    }
}
