package com.dingyabin.rocket.helper;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;

@Slf4j
public class RocketSenderHelper {

    private static DefaultMQProducer defaultMQProducer;


    public static SendResult send(String topic, String body) {
        try {
            Message message = new Message(topic, body.getBytes(StandardCharsets.UTF_8));
            return defaultMQProducer.send(message);
        } catch (Exception e) {
            log.error("RocketSenderHelper.send error...", e);
        }
        return null;
    }


    public static void initDefaultMQProducer(DefaultMQProducer defaultMQProducer) {
        RocketSenderHelper.defaultMQProducer = defaultMQProducer;
    }
}
