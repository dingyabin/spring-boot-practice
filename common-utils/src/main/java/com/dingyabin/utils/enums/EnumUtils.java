package com.dingyabin.utils.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EnumUtils {

    // 缓存 Key: 枚举Class, Value: Map<Code, 枚举实例>
    private static final Map<Class<?>, Map<Object, Enum<?>>> ENUM_CACHE = new ConcurrentHashMap<>();


    @SuppressWarnings("unchecked")
    public static <T, E extends Enum<E> & BaseEnum<T>> E getEnumByCode(Class<E> enumType, T code) {
        if (code == null) {
            return null;
        }
        // 获取或创建该枚举类型的 Code映射表
        Map<Object, Enum<?>> cache = ENUM_CACHE.computeIfAbsent(enumType, k -> {
            Map<Object, Enum<?>> map = new ConcurrentHashMap<>();
            for (E e : enumType.getEnumConstants()) {
                map.put(e.getCode(), e);
            }
            return map;
        });
        return (E) cache.get(code);
    }


    public static <E extends Enum<E> & BaseEnum<T>, T> String getEnumValueByCode(Class<E> enumType, T code) {
        E enumValue = getEnumByCode(enumType, code);
        if (enumValue == null) {
            return "";
        }
        return enumValue.getValue();
    }
}
