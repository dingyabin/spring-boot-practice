package com.dingyabin.util;

import cn.hutool.core.util.ReflectUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.util.Objects;

public class ModelUtil {


    public static void replaceNullLocalFields(Object object, String replacement, String... ignoreFields) {
        ReflectionUtils.doWithLocalFields(object.getClass(), field -> {
            //如果是忽略的字段，直接终止
            if (Objects.nonNull(ignoreFields) && ArrayUtils.contains(ignoreFields, field.getName())) {
                return;
            }
            //string类型的并且值是null的才处理
            if (field.getType().equals(String.class) && Objects.isNull(ReflectUtil.getFieldValue(object, field))) {
                ReflectUtil.setFieldValue(object, field, replacement);
            }
        });
    }


    public static void replaceNullLocalFieldsByEmpty(Object object) {
        replaceNullLocalFields(object, StringUtils.EMPTY);
    }


    public static void replaceNullLocalFieldsByEmpty(Object object, String... ignoreFields) {
        replaceNullLocalFields(object, StringUtils.EMPTY, ignoreFields);
    }

}
