package com.dingyabin.config.mybatis;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.dingyabin.config.annotation.EncryptField;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;

import java.lang.reflect.Field;
import java.util.List;


@AutoConfiguration(after = MybatisPlusAutoConfiguration.class)
public class MybatisPlusHelperConfiguration implements InitializingBean {



    @Override
    public void afterPropertiesSet() {
        List<TableInfo> tableInfos = TableInfoHelper.getTableInfos();
        for (TableInfo tableInfo : tableInfos) {
            findEncryptAnnotation(tableInfo);
        }
    }


    private static void findEncryptAnnotation(TableInfo tableInfo) {
        Class<?> entityType = tableInfo.getEntityType();
        for (TableFieldInfo fieldInfo : tableInfo.getFieldList()) {
            Field field = fieldInfo.getField();
            EncryptField annotation;
            if ((annotation = field.getAnnotation(EncryptField.class)) != null) {
                MybatisMetaHelper.registerEncryptField(entityType, field, annotation);
            }
        }
    }


}
