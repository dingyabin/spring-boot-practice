package com.dingyabin.redis.config;

import com.dingyabin.redis.listener.BaseRedisListenerConfig;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 丁亚宾
 * Date: 2024/8/15.
 * Time:21:44
 */
@Configuration
public class RedisListenerConfig extends BaseRedisListenerConfig {


    @Override
    protected Executor createTaskExecutor() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                2,
                10,
                20,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(100)
        );
        BasicThreadFactory threadFactory = new BasicThreadFactory.Builder().namingPattern("Redis-listener-thread-%s").build();
        threadPoolExecutor.setThreadFactory(threadFactory);
        return threadPoolExecutor;
    }
}
