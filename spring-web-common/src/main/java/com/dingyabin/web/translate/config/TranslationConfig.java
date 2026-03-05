package com.dingyabin.web.translate.config;

import com.dingyabin.web.translate.annotation.TranslationType;
import com.dingyabin.web.translate.core.TranslationInterface;
import com.dingyabin.web.translate.core.handler.TranslationBeanSerializerModifier;
import com.dingyabin.web.translate.core.handler.TranslationHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 翻译配置类
 *
 */
@Slf4j
@AutoConfiguration
public class TranslationConfig {


    private final List<TranslationInterface<?>> list;


    private final ObjectMapper objectMapper;


    public TranslationConfig(
            @Autowired(required = false) List<TranslationInterface<?>> list,
            @Autowired(required = false) ObjectMapper objectMapper) {
        this.list = list;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    public void init() {
        Map<String, TranslationInterface<?>> map = new HashMap<>(list.size());
        for (TranslationInterface<?> trans : list) {
            if (trans.getClass().isAnnotationPresent(TranslationType.class)) {
                TranslationType annotation = trans.getClass().getAnnotation(TranslationType.class);
                map.put(annotation.type(), trans);
            } else {
                log.warn(trans.getClass().getName() + " 翻译实现类未标注 TranslationType 注解!");
            }
        }
        TranslationHandler.TRANSLATION_MAPPER.putAll(map);
        // 设置 Bean 序列化修改器
        objectMapper.setSerializerFactory(
            objectMapper.getSerializerFactory().withSerializerModifier(new TranslationBeanSerializerModifier())
        );
    }

}
