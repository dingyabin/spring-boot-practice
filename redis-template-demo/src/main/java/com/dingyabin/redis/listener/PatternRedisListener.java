package com.dingyabin.redis.listener;

import com.dingyabin.redis.listener.api.BaseRedisMessageListener;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author 丁亚宾
 * Date: 2024/8/15.
 * Time:22:42
 */
@Component
public class PatternRedisListener extends BaseRedisMessageListener {


    @Override
    public Collection<? extends Topic> topics() {
        return Collections.singleton(PatternTopic.of("first-channel*"));
    }


    @Override
    protected void handleMessage(String message, String channel, byte[] pattern) {
        System.out.println(Thread.currentThread().getName() + " --收到消息: " + message + " --channel: " + channel + " --pattern:" + new String(pattern));
    }
}
