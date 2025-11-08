package com.dingyabin.rocket.config;

import com.dingyabin.rocket.helper.AbstractListener;
import com.dingyabin.rocket.helper.RocketListener;
import com.dingyabin.rocket.helper.RocketSenderHelper;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

@Configuration
public class RocketConfig implements BeanPostProcessor {

    @Value("${rocket.nameServ}")
    private String nameServ;

    @Value("${rocket.product.groupName}")
    private String productGroupName;

    @Value("${rocket.consume.groupName}")
    private String consumeGroupName;

    @Bean
    public DefaultMQProducer defaultMQProducer() throws MQClientException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer(productGroupName);
        defaultMQProducer.setNamesrvAddr(nameServ);
        defaultMQProducer.start();
        RocketSenderHelper.initDefaultMQProducer(defaultMQProducer);
        return defaultMQProducer;
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RocketListener annotation;
        if (bean instanceof AbstractListener && (annotation = findAnnotation(bean.getClass(), RocketListener.class)) != null) {
            try {
                DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(consumeGroupName);
                defaultMQPushConsumer.setNamesrvAddr(nameServ);
                //默认的值就是这个
                defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);

                defaultMQPushConsumer.subscribe(annotation.topic(), "*");

                defaultMQPushConsumer.setConsumeMessageBatchMaxSize(2);

                defaultMQPushConsumer.registerMessageListener((AbstractListener) bean);
                defaultMQPushConsumer.start();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
