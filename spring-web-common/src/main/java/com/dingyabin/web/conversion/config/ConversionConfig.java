package com.dingyabin.web.conversion.config;

import cn.hutool.core.collection.CollUtil;
import com.dingyabin.web.conversion.annotation.ConversionType;
import com.dingyabin.web.conversion.core.ConversionInterface;
import com.dingyabin.web.conversion.core.handler.ConversionBeanSerializerModifier;
import com.dingyabin.web.conversion.core.handler.ConversionSerializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 翻译配置类
 */
@Slf4j
@AutoConfiguration
public class ConversionConfig {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer conversionConfig(@Autowired(required = false) List<ConversionInterface<?>> list) {
        if (CollUtil.isNotEmpty(list)) {
            for (ConversionInterface<?> trans : list) {
                ConversionType annotation = AnnotatedElementUtils.findMergedAnnotation(trans.getClass(), ConversionType.class);
                if (Objects.nonNull(annotation)) {
                    ConversionSerializer.TRANSLATION_MAPPER.put(annotation.type(), trans);
                }
                else {
                    log.warn("{} 转换实现类未标注 ConversionType 注解!", trans.getClass().getName());
                }
            }
        }
        //注册BeanSerializerModifier
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setSerializerModifier(new ConversionBeanSerializerModifier());
        return builder -> builder.modulesToInstall(modules -> modules.add(simpleModule));
    }

}
