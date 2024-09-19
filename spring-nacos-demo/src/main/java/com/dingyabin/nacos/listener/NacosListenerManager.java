package com.dingyabin.nacos.listener;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:22:12
 */
@Service
public class NacosListenerManager implements InitializingBean {

    @Resource
    private NacosConfigManager nacosConfigManager;

    private final List<BaseNacosConfigChangeListener> iNacosConfigChangeListeners;

    @Autowired(required = false)
    public NacosListenerManager(List<BaseNacosConfigChangeListener> iNacosConfigChangeListeners) {
        this.iNacosConfigChangeListeners = iNacosConfigChangeListeners;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        if (CollectionUtils.isEmpty(iNacosConfigChangeListeners)) {
            return;
        }
        for (BaseNacosConfigChangeListener listener : iNacosConfigChangeListeners) {
            nacosConfigManager.getConfigService().addListener(listener.subscribeDataId(), listener.subscribeGroup(), new AbstractConfigChangeListener() {
                @Override
                public void receiveConfigChange(ConfigChangeEvent event) {
                    listener.receiveConfigChange(event);
                }
            });

        }
    }


}
