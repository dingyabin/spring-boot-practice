package com.dingyabin.prometheusdemo.service;

import com.dingyabin.prometheusdemo.aop.model.InvocationModel;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Administrator
 * Date: 2024/7/13.
 * Time:23:10
 */
@Slf4j
@Service
public class InvocationMonitorService {

    @Resource
    private MeterRegistry meterRegistry;


    public void counterIncr(InvocationModel model) {
        counterIncr(model.getName(), "signature", model.getShortClassName() + "." + model.getMethodName());
    }


    public void counterIncr(String name, String... tags) {
        try {
            meterRegistry.counter(name, tags).increment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void counterWithRetIncr(InvocationModel model, boolean success) {
        counterWithRetIncr(model.getName(), success, "signature", model.getShortClassName() + "." + model.getMethodName());
    }


    public void counterWithRetIncr(String name, boolean success, String... tags) {
        try {
            Counter.builder(name)
                    .tags(tags)
                    .tags(Tags.of("result", String.valueOf(success)))
                    .register(meterRegistry).increment();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void timerDuration(InvocationModel model, long cost) {
        timerDuration(model.getName(), cost, "signature", model.getShortClassName() + "." + model.getMethodName());
    }


    public void timerDuration(String name, long cost, String... tags) {
        try {
            meterRegistry.timer(name, tags).record(cost, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
