package com.dingyabin.cache.helper;

import com.dingyabin.cache.helper.api.BaseCaffeineCacheManager;
import com.dingyabin.cache.model.Student;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/8/23.
 * Time:0:30
 */
@Service
public class StudentCacheManager extends BaseCaffeineCacheManager<String, Student> {


    @Override
    protected Caffeine<Object, Object> customizers(Caffeine<Object, Object> kvCaffeine) {
        kvCaffeine.maximumSize(500)
                .expireAfterWrite(10, TimeUnit.SECONDS);
        return kvCaffeine;
    }



    @Override
    protected Student generateObject(String key) {
        return new Student(key,10);
    }


}
