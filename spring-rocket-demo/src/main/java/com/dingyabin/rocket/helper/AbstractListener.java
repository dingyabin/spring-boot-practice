package com.dingyabin.rocket.helper;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractListener implements MessageListenerConcurrently {


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
        List<String> list = new ArrayList<>();
        for (MessageExt messageExt : msgs) {
            list.add(new String(messageExt.getBody()));
        }
        return handleMessage(list, context);
    }


    public abstract ConsumeConcurrentlyStatus handleMessage(List<String> msg, ConsumeConcurrentlyContext context);

}
