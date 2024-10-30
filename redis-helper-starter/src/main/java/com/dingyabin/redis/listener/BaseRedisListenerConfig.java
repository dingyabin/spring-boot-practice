package com.dingyabin.redis.listener;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author 丁亚宾
 * Date: 2024/8/15.
 * Time:21:44
 */
public abstract class BaseRedisListenerConfig {


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


    protected abstract Executor createTaskExecutor();


}
