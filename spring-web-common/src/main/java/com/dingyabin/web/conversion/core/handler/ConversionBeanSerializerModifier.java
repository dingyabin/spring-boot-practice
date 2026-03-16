package com.dingyabin.web.conversion.core.handler;

import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;

import java.util.List;

/**
 * Bean 序列化修改器 解决 Null 被单独处理问题
 *
 * @author Lion Li
 */
public class ConversionBeanSerializerModifier extends BeanSerializerModifier {

    @Override
    public List<BeanPropertyWriter> changeProperties(SerializationConfig config, BeanDescription beanDesc,
                                                     List<BeanPropertyWriter> beanProperties) {
        for (BeanPropertyWriter writer : beanProperties) {
            // 如果序列化器为 ConversionSerializer 的话 将 Null 值也交给他处理
            JsonSerializer<Object> serializer = writer.getSerializer();
            if (serializer instanceof ConversionSerializer) {
                writer.assignNullSerializer(serializer);
            }
        }
        return beanProperties;
    }

}
