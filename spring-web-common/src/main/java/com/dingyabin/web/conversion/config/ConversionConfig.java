package com.dingyabin.web.conversion.config;

import cn.hutool.core.collection.CollUtil;
import com.dingyabin.web.conversion.annotation.Conversion;
import com.dingyabin.web.conversion.annotation.ConversionType;
import com.dingyabin.web.conversion.core.ConversionInterface;
import com.dingyabin.web.conversion.core.handler.ConversionSerializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.util.List;
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
        SimpleModule simpleModule = buildSimpleModule();
        return builder -> builder.modulesToInstall(modules -> modules.add(simpleModule));
    }




    private SimpleModule buildSimpleModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                for (BeanPropertyWriter writer : beanProperties) {
                    // 如果序列化器为 ConversionSerializer 的话 将 Null 值也交给他处理
                    JsonSerializer<Object> serializer = writer.getSerializer();
                    if (serializer instanceof ConversionSerializer || writer.getAnnotation(Conversion.class) != null) {
                        writer.assignNullSerializer(serializer);
                    }
                }
                return beanProperties;
            }
        });
        return simpleModule;
    }

}
