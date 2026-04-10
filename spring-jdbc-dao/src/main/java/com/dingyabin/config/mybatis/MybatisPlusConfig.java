package com.dingyabin.config.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.dingyabin.config.mybatis.intercept.MybatisDecryptInterceptor;
import com.dingyabin.config.mybatis.intercept.MybatisEncryptInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisPlusConfig {


    /**
     * 分页插件配置
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setDbType(DbType.MYSQL); // 数据库类型
        paginationInnerInterceptor.setOverflow(true); // 溢出总页数后是否进行处理
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
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

}
