package com.dingyabin.config.mybatis;


import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.dingyabin.config.annotation.EncryptField;
import com.dingyabin.util.EncryptHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Slf4j
public class MybatisMetaHelper {

    private static final Map<Class<?>, Set<Pair<Field, EncryptField>>> ENCRYPT_FIELD_CACHE_MAP = new ConcurrentHashMap<>();


    public static void registerEncryptField(Class<?> clz, Field field, EncryptField encryptField) {
        ENCRYPT_FIELD_CACHE_MAP.computeIfAbsent(clz, k -> new HashSet<>()).add(Pair.of(field, encryptField));
    }


    public static Set<Pair<Field, EncryptField>> getEncryptField(Class<?> clz) {
        return ENCRYPT_FIELD_CACHE_MAP.get(clz);
    }


    public static boolean hasEncryptField(Class<?> clz) {
        return CollectionUtils.isNotEmpty(getEncryptField(clz));
    }

    public static boolean hasNoEncryptField(Class<?> clz) {
        return !hasEncryptField(clz);
    }


    /**
     * 待加解密对象
     *
     * @param sourceObject 待加解密对象
     */
    public static void tryDealObject(Object sourceObject, Consumer<Object> consumer) {
        if (Objects.isNull(sourceObject)) {
            return;
        }
        if (sourceObject instanceof Map<?, ?>) {
            Map<?, ?> map = (Map<?, ?>) sourceObject;
            new HashSet<>(map.values()).forEach(consumer);
            return;
        }
        if (sourceObject instanceof Collection<?>) {
            Collection<?> collection = (Collection<?>) sourceObject;
            if (CollUtil.isEmpty(collection)) {
                return;
            }
            // 判断第一个元素是否含有注解。如果没有直接返回，提高效率
            Object firstItem = collection.iterator().next();
            if (ObjectUtil.isNull(firstItem) || MybatisMetaHelper.hasNoEncryptField(firstItem.getClass())) {
                return;
            }
            collection.forEach(consumer);
            return;
        }
        // 不在缓存中的类,就是没有加密注解的类
        consumer.accept(sourceObject);
    }

    /**
     * 字段值进行加密。通过字段的批注注册新的加密算法
     */
    public static void encryptObject(Object object, String key) {
        if (ObjectUtil.isNull(object)) {
            return;
        }
        Set<Pair<Field, EncryptField>> encryptFields = MybatisMetaHelper.getEncryptField(object.getClass());
        if (CollectionUtils.isEmpty(encryptFields)) {
            return;
        }
        for (Pair<Field, EncryptField> fieldPair : encryptFields) {
            try {
                Field field = fieldPair.getKey();
                Object fieldValue = ReflectUtil.getFieldValue(object, field);
                if (Objects.isNull(fieldValue)) {
                    continue;
                }
                ReflectUtil.setFieldValue(object, field, EncryptHelper.encrypt(fieldValue.toString(), key));
            } catch (Exception e) {
                log.error("加密出错, object={}....", object.getClass(), e);
            }
        }
    }


    /**
     * 字段值进行解密。通过字段的批注注册新的解密算法
     */
    public static void decryptObject(Object object, String key) {
        if (ObjectUtil.isNull(object)) {
            return;
        }
        Set<Pair<Field, EncryptField>> encryptFields = MybatisMetaHelper.getEncryptField(object.getClass());
        if (CollectionUtils.isEmpty(encryptFields)) {
            return;
        }
        for (Pair<Field, EncryptField> fieldPair : encryptFields) {
            try {
                Field field = fieldPair.getKey();
                Object fieldValue = ReflectUtil.getFieldValue(object, field);
                if (Objects.isNull(fieldValue)) {
                    continue;
                }
                String encrypt = fieldValue.toString();
                if (EncryptHelper.isEncrypt(encrypt)) {
                    ReflectUtil.setFieldValue(object, field, EncryptHelper.decrypt(encrypt, key));
                }
            } catch (Exception e) {
                log.error("解密出错, object={}....", object.getClass(), e);
            }
        }
    }


}
