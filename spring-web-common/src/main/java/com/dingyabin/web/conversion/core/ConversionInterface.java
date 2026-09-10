package com.dingyabin.web.conversion.core;


import com.dingyabin.web.conversion.core.handler.ConversionSerializer;
import org.springframework.beans.factory.InitializingBean;

/**
 * 转换接口
 *
 * @author Lion Li
 */
public interface ConversionInterface<T> extends InitializingBean {

    /**
     * 转换类型
     */
    String conversionType();

    /**
     * 转换
     *
     * @param key   需要被转换的键(不为空)
     * @param other 其他参数
     * @return 返回键对应的值
     */
    T translation(Object key, String other);



    @Override
    default void afterPropertiesSet() {
        ConversionSerializer.TRANSLATION_MAPPER.put(conversionType(), this);
    }

}
