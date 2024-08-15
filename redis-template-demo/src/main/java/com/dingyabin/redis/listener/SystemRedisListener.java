package com.dingyabin.redis.listener;

import com.dingyabin.redis.listener.api.BaseRedisMessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.Topic;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;

/**
 * @author 丁亚宾
 * Date: 2024/8/15.
 * Time:21:43
 */
@Component
public class SystemRedisListener extends BaseRedisMessageListener {


    @Override
    public Collection<? extends Topic> topicToBeSubscribed() {
        return Collections.singleton(ChannelTopic.of("first-channel"));
    }


    @Override
    public void handleMessage(String message, String channel, byte[] pattern) {
        System.out.println(Thread.currentThread().getName() + " --收到消息: " + message + "--channel: " + channel);

    }


}
