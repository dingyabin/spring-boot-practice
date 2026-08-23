package com.dingyabin.web.dynamic;

import com.dingyabin.web.dynamic.anno.DynamicProperty;
import com.dingyabin.web.dynamic.writer.DynamicBeanPropertyWriter;
import com.fasterxml.jackson.databind.SerializerProvider;

public interface DynamicPropertyResolverInterface {

    boolean shouldSerializeProperty(Object bean, SerializerProvider prov, String propertyName, DynamicBeanPropertyWriter propertyWriter, DynamicProperty dynamicProperty);

}
