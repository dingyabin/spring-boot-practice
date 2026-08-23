package com.dingyabin.web.dynamic.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.dingyabin.web.dynamic.DynamicPropertyResolverInterface;
import com.dingyabin.web.dynamic.anno.DynamicProperty;
import com.dingyabin.web.dynamic.anno.DynamicPropertyModel;
import com.dingyabin.web.dynamic.writer.DynamicBeanPropertyWriter;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Configuration
public class PropertyWriterConfig {


    @Bean
    public Jackson2ObjectMapperBuilderCustomizer propertyWriterConfig() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.setSerializerModifier(new BeanSerializerModifier() {
            @Override
            public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> beanProperties) {
                DynamicPropertyModel propertyModel = beanDesc.getBeanClass().getAnnotation(DynamicPropertyModel.class);
                //没有注解的，不处理
                if (Objects.isNull(propertyModel)) {
                    return beanProperties;
                }
                DynamicPropertyResolverInterface dynamicPropertyResolver = null;
                String beanName = propertyModel.resolverBeanName();
                if (StrUtil.isNotBlank(beanName)) {
                    dynamicPropertyResolver = SpringUtil.getBean(beanName);
                }
                if (dynamicPropertyResolver == null && !propertyModel.resolverBeanClass().equals(DynamicPropertyResolverInterface.class)) {
                    dynamicPropertyResolver = SpringUtil.getBean(propertyModel.resolverBeanClass());
                }
                //没找到dynamicPropertyResolver, 按照策略执行操作
                if (dynamicPropertyResolver == null) {
                    log.error("DynamicPropertyResolver not found for {}", beanDesc.getBeanClass().getName());
                    //如果配置了忽略，则直接返回
                    if (!propertyModel.resolverCanNull()) {
                        throw new RuntimeException("DynamicPropertyResolver not found for " + beanDesc.getBeanClass().getName());
                    }
                    return beanProperties;
                }
                DynamicPropertyModel.PropertyStrategy propertyStrategy = propertyModel.propertyStrategy();
                //替换成包装类
                List<BeanPropertyWriter> list = new ArrayList<>(beanProperties.size());
                for (BeanPropertyWriter beanProperty : beanProperties) {
                    //根据策略判断是否需要动态属性
                    boolean shouldDynamic = propertyStrategy.shouldDynamic(beanProperty);
                    DynamicProperty dynamicProperty = beanProperty.getAnnotation(DynamicProperty.class);
                    list.add(shouldDynamic ? new DynamicBeanPropertyWriter(beanProperty, dynamicPropertyResolver, dynamicProperty) : beanProperty);
                }
                return list;
            }
        });
        return builder -> builder.modulesToInstall(modules -> modules.add(simpleModule));
    }

}
