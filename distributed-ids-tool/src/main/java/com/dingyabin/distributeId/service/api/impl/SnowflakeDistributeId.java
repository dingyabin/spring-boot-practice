package com.dingyabin.distributeId.service.api.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.dingyabin.distributeId.model.IpWorkerConfig;
import com.dingyabin.distributeId.service.IpWorkerConfigService;
import com.dingyabin.distributeId.service.api.IDistributeId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author Administrator
 * Date: 2024/9/30.
 * Time:16:28
 */
@Service
public class SnowflakeDistributeId implements IDistributeId, InitializingBean {

    @Resource
    private IpWorkerConfigService ipWorkerConfigService;


    private Snowflake snowflake;


    @Override
    public void afterPropertiesSet() throws Exception {
        IpWorkerConfig localIpWorkerConfig = ipWorkerConfigService.getLocalIpWorkerConfig();
        snowflake = (localIpWorkerConfig != null) ? new Snowflake(localIpWorkerConfig.getWorkId()) : new Snowflake();
    }


    @Override
    public Long nextId() {
        return snowflake.nextId();
    }


    @Override
    public Long nextId(String bizType) {
        return nextId();
    }

}
