package com.dingyabin.distributeId.service;

import cn.hutool.core.net.NetUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.distributeId.model.IpWorkerConfig;
import com.dingyabin.distributeId.mapper.IpWorkerConfigMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 *
 */
@Service
public class IpWorkerConfigService extends ServiceImpl<IpWorkerConfigMapper, IpWorkerConfig> implements InitializingBean {


    private String localhostIp;


    @Override
    public void afterPropertiesSet() {
        localhostIp = NetUtil.getLocalhostStr();
    }


    public IpWorkerConfig getLocalIpWorkerConfig() {
        if (StringUtils.isBlank(localhostIp)) {
            return null;
        }
        LambdaQueryWrapper<IpWorkerConfig> wrapper = Wrappers.lambdaQuery(IpWorkerConfig.class)
                .eq(IpWorkerConfig::getIp, localhostIp);
        return getOne(wrapper);
    }

}




