package com.dingyabin.config.mybatis;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.dingyabin.config.annotation.EncryptField;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.tokenizeToStringArray;


@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
public class MybatisPlusHelperConfiguration implements InitializingBean {

    private final MybatisPlusProperties properties;

    public MybatisPlusHelperConfiguration(MybatisPlusProperties properties) {
        this.properties = properties;
    }


    @Override
    public void afterPropertiesSet() {
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        for (TableInfo tableInfo : tableInfos) {
            findEncryptAnnotation(tableInfo);
        }
        String typeAliasesPackage = properties.getTypeAliasesPackage();
        if (StringUtils.isNotEmpty(typeAliasesPackage)) {
            String[] packagePatternArray = tokenizeToStringArray(typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
            Stream.of(packagePatternArray).forEach(packagePattern -> {
                Set<Class<?>> classes = ClassUtil.scanPackage(packagePattern,
                        clazz -> !clazz.isAnonymousClass() && !clazz.isInterface() && !clazz.isMemberClass());
                classes.forEach(clazz -> {
                    Arrays.stream(ReflectUtil.getFields(clazz)).forEach(field -> findEncryptAnnotation(field, clazz));
                });
            });
        }
    }


    private static void findEncryptAnnotation(TableInfo tableInfo) {
        Class<?> entityType = tableInfo.getEntityType();
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            findEncryptAnnotation(fieldInfo.getField(), entityType);
        }
    }


    private static void findEncryptAnnotation(Field field, Class<?> entityType) {
        EncryptField annotation;
        if ((annotation = field.getAnnotation(EncryptField.class)) != null) {
            MybatisMetaHelper.registerEncryptField(entityType, field, annotation);
        }
    }


}
