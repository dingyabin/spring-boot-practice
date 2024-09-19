package com.dingyabin.nacos.listener;

import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:22:33
 */
public abstract class BaseNacosConfigChangeListener {


    public abstract String subscribeDataId();


    public abstract String subscribeGroup();


    public abstract List<String> subscribeKeys();


    public void receiveConfigChange(ConfigChangeEvent event) {
        //不需要过滤key
        if (CollectionUtils.isEmpty(subscribeKeys())) {
            handleConfigChange(event, Collections.emptyList());
            return;
        }
        //过滤key，找出子类订阅的key
        List<ConfigChangeItem> list = subscribeKeys().stream()
                .map(event::getChangeItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        handleConfigChange(event, list);
    }


    public abstract void handleConfigChange(ConfigChangeEvent event, List<ConfigChangeItem> list);
}
