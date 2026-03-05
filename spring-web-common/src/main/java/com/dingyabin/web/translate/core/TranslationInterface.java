package com.dingyabin.web.translate.core;



/**
 * 翻译接口
 *
 * @author Lion Li
 */
public interface TranslationInterface<T> {
    /**
     * 翻译
     *
     * @param key   需要被翻译的键(不为空)
     * @param other 其他参数
     * @return 返回键对应的值
     */
    T translation(Object key, String other);
}
