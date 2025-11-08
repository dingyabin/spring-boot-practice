package com.dingyabin.rocket.test;

import com.dingyabin.rocket.helper.AbstractListener;
import com.dingyabin.rocket.helper.RocketListener;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;

import java.util.List;

@RocketListener(topic = "TEST_TOPIC")
public class SimpleRocketListener extends AbstractListener {


    @Override
    public ConsumeConcurrentlyStatus handleMessage(List<String> msgs, ConsumeConcurrentlyContext context) {
        for (String str : msgs) {
            System.out.println(str);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
