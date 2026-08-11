package com.dingyabin.web.dynamic.writer;

import com.dingyabin.web.dynamic.DynamicPropertyResolverInterface;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;

public class DynamicBeanPropertyWriter extends BeanPropertyWriter {

    private final DynamicPropertyResolverInterface dynamicPropertyResolver;

    public DynamicBeanPropertyWriter(BeanPropertyWriter base, DynamicPropertyResolverInterface dynamicPropertyResolver) {
        super(base);
        this.dynamicPropertyResolver = dynamicPropertyResolver;
    }


    @Override
    public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov) throws Exception {
        //只有经过逻辑判断需要输出的, 才序列化输出
        if (dynamicPropertyResolver.shouldSerializeProperty(bean, prov, getName(), this)) {
            super.serializeAsField(bean, gen, prov);
        }
    }
}
