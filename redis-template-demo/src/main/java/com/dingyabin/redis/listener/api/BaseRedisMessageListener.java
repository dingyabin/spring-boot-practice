package com.dingyabin.redis.listener.api;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.Topic;

import java.util.Collection;

/**
 * @author 丁亚宾
 * Date: 2024/8/15.
 * Time:22:24
 */
public abstract class BaseRedisMessageListener implements MessageListener {

    /**
     * 订阅的主题是什么
     * @return
     */
   public abstract Collection<? extends Topic> topicToBeSubscribed();



    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleMessage(new String(message.getBody()), new String(message.getChannel()), pattern);
    }



    protected abstract void handleMessage(String message, String channel, byte[] pattern);

}
