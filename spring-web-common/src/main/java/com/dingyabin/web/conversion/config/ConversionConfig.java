package com.dingyabin.web.conversion.config;

import com.dingyabin.web.conversion.annotation.ConversionType;
import com.dingyabin.web.conversion.core.ConversionInterface;
import com.dingyabin.web.conversion.core.handler.ConversionBeanSerializerModifier;
import com.dingyabin.web.conversion.core.handler.ConversionSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 翻译配置类
 *
 */
@Slf4j
@AutoConfiguration
public class ConversionConfig {


    private final List<ConversionInterface<?>> list;


    private final ObjectMapper objectMapper;


    public ConversionConfig(
            @Autowired(required = false) List<ConversionInterface<?>> list,
            @Autowired(required = false) ObjectMapper objectMapper) {
        this.list = list;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        Map<String, ConversionInterface<?>> map = new HashMap<>(list.size());
        for (ConversionInterface<?> trans : list) {
            if (trans.getClass().isAnnotationPresent(ConversionType.class)) {
                ConversionType annotation = trans.getClass().getAnnotation(ConversionType.class);
                map.put(annotation.type(), trans);
            } else {
                log.warn(trans.getClass().getName() + " 转换实现类未标注 ConversionType 注解!");
            }
        }
        ConversionSerializer.TRANSLATION_MAPPER.putAll(map);
        // 设置 Bean 序列化修改器
        if (Objects.nonNull(objectMapper)) {
            objectMapper.setSerializerFactory(objectMapper.getSerializerFactory().withSerializerModifier(new ConversionBeanSerializerModifier()));
        }
    }

}
