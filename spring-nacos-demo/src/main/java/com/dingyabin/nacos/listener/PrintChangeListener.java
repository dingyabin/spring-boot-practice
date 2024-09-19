package com.dingyabin.nacos.listener;

import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:22:42
 */
@Service
public class PrintChangeListener extends BaseNacosConfigChangeListener {

    @Override
    public String subscribeDataId() {
        return "spring-nacos-demo.yaml";
    }

    @Override
    public String subscribeGroup() {
        return "SPRING-NACOS-DEMO";
    }

    @Override
    public List<String> subscribeKeys() {
        return Arrays.asList("aa");
    }

    @Override
    public void handleConfigChange(ConfigChangeEvent event, List<ConfigChangeItem> list) {
        for (ConfigChangeItem changeItem : event.getChangeItems()) {
            System.out.println(changeItem.getKey() + " 从 " + changeItem.getOldValue() + " 变为 " + changeItem.getNewValue());
        }
        System.out.println("订阅的key变化：" + list);
    }
}
