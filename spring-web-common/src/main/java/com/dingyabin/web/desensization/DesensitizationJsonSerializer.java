package com.dingyabin.web.desensization;

import com.dingyabin.web.desensization.enums.DesensitizedTypeEnum;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

/**
 * @author 丁亚宾
 * Date: 2024/11/8.
 * Time:14:40
 */
public class DesensitizationJsonSerializer extends JsonSerializer<String> implements ContextualSerializer {

    private DesensitizedTypeEnum desensitizedType;

    public DesensitizationJsonSerializer() {
    }

    public DesensitizationJsonSerializer(DesensitizedTypeEnum desensitizedType) {
        this.desensitizedType = desensitizedType;
    }

    @Override
    public void serialize(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(desensitizedType.getDesensitize().apply(value));
    }


    @Override
    public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property) throws JsonMappingException {
        Desensitization desensitization = property.getAnnotation(Desensitization.class);
        JavaType type = property.getType();
        if (desensitization != null && String.class.equals(type.getRawClass())) {
            return new DesensitizationJsonSerializer(desensitization.value());
        }
        return prov.findValueSerializer(type, property);
    }
}
