package com.dingyabin.config.mybatis;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.dingyabin.config.annotation.EncryptField;
import com.dingyabin.config.mybatis.intercept.MybatisDecryptInterceptor;
import com.dingyabin.config.mybatis.intercept.MybatisEncryptInterceptor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

@AutoConfiguration
@ConditionalOnProperty(prefix = MybatisPlusEncryptProperties.ENCRYPT_PREFIX, name = "enable", havingValue = "true")
public class MybatisPlusHelperConfiguration implements InitializingBean {

    private final MybatisPlusProperties properties;


    public MybatisPlusHelperConfiguration(MybatisPlusProperties properties) {
        this.properties = properties;
    }

    @Bean
    public MybatisPlusEncryptProperties mybatisPlusEncryptProperties() {
        return new MybatisPlusEncryptProperties();
    }


    /**
     * 加密组件
     */
    @Bean
    public MybatisEncryptInterceptor mybatisEncryptInterceptor() {
        return new MybatisEncryptInterceptor();
    }


    /**
     * 解密组件
     */
    @Bean
    public MybatisDecryptInterceptor mybatisDecryptInterceptor() {
        return new MybatisDecryptInterceptor();
    }


    @Override
    public void afterPropertiesSet() {
        String typeAliasesPackage = properties.getTypeAliasesPackage();
        if (StringUtils.isEmpty(typeAliasesPackage)) {
            return;
        }
        String[] packageArray = tokenizeToStringArray(typeAliasesPackage, ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);
        Stream.of(packageArray).forEach(pattern -> {
            Set<Class<?>> clazzSet = ClassUtil.scanPackage(pattern, clazz -> !clazz.isAnonymousClass() && !clazz.isInterface() && !clazz.isMemberClass());
            clazzSet.forEach(clazz -> {
                Arrays.stream(ReflectUtil.getFields(clazz)).forEach(field -> findEncryptAnnotation(field, clazz));
            });
        });
    }


    private static void findEncryptAnnotation(Field field, Class<?> entityType) {
        EncryptField annotation;
        if ((annotation = field.getAnnotation(EncryptField.class)) != null) {
            MybatisMetaHelper.registerEncryptField(entityType, field, annotation);
        }
    }


}
