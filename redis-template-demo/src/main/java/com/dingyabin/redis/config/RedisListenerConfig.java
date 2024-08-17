package com.dingyabin.redis.config;

import com.dingyabin.redis.listener.api.BaseRedisMessageListener;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;
import java.util.List;
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
public class RedisListenerConfig {


    @Resource
    private List<BaseRedisMessageListener> baseRedisMessageListeners;


    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(redisConnectionFactory);
        listenerContainer.setTaskExecutor(createTaskExecutor());
        for (BaseRedisMessageListener listener : baseRedisMessageListeners) {
            listenerContainer.addMessageListener(listener, listener.topics());
        }
        return listenerContainer;
    }


    private Executor createTaskExecutor() {
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
