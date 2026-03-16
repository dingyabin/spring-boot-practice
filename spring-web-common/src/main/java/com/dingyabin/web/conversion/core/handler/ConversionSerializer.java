package com.dingyabin.web.conversion.core.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.dingyabin.web.conversion.annotation.Conversion;
import com.dingyabin.web.conversion.core.ConversionInterface;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 翻译处理器
 *
 * @author Lion Li
 */
@Slf4j
public class ConversionSerializer extends JsonSerializer<Object> implements ContextualSerializer {

    /**
     * 全局翻译实现类映射器
     */
    public static final Map<String, ConversionInterface<?>> TRANSLATION_MAPPER = new ConcurrentHashMap<>();

    private Conversion conversion;

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        ConversionInterface<?> trans = TRANSLATION_MAPPER.get(conversion.conversionType());
        if (ObjectUtil.isNotNull(trans)) {
            // 如果映射字段不为空 则取映射字段的值
            if (StringUtils.hasText(conversion.mapper())) {
                value = ReflectUtil.getFieldValue(gen.currentValue(), conversion.mapper());
            }
            // 如果为 null 直接写出
            if (ObjectUtil.isNull(value)) {
                gen.writeNull();
                return;
            }
            try {
                Object result = trans.translation(value, conversion.extra());
                gen.writeObject(result);
            } catch (Exception e) {
                log.error("翻译处理异常，type: {}, value: {}", conversion.conversionType(), value, e);
                // 出现异常时输出原始值而不是中断序列化
                gen.writeObject(value);
            }
        } else {
            gen.writeObject(value);
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Conversion convert = property.getAnnotation(Conversion.class);
        if (Objects.nonNull(convert)) {
            this.conversion = convert;
            return this;
        }
        return prov.findValueSerializer(property.getType(), property);
    }
}
