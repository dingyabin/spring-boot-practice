package com.dingyabin.distributeId.vo;

import cn.hutool.core.util.RandomUtil;
import com.dingyabin.web.dynamic.DynamicPropertyResolverInterface;
import com.dingyabin.web.dynamic.anno.DynamicProperty;
import com.dingyabin.web.dynamic.writer.DynamicBeanPropertyWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Service;

@Service
public class TestDynamicPropertyResolver implements DynamicPropertyResolverInterface {

    @Override
    public boolean shouldSerializeProperty(Object bean, SerializerProvider prov, String propertyName, DynamicBeanPropertyWriter propertyWriter, DynamicProperty dynamicProperty) {
        return RandomUtil.randomBoolean();
    }
}
