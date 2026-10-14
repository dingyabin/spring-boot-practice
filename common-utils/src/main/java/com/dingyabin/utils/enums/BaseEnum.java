package com.dingyabin.utils.enums;

public interface BaseEnum<T> {
    /**
     * 获取枚举的Code
     */
    T getCode();

    /**
     * 获取枚举的Value
     */
    String getValue();
}
