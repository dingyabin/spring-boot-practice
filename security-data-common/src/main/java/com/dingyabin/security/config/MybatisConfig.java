package com.dingyabin.security.config;

import com.dingyabin.security.service.*;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 丁亚宾
 * Date: 2024/8/1.
 * Time:23:57
 */
@Configuration
@MapperScan("com.dingyabin.security.mapper")
public class MybatisConfig {


    @Bean
    public SysAuthorityService sysAuthorityService() {
        return new SysAuthorityService();
    }


    @Bean
    public SysRoleAuthorityService sysRoleAuthorityService() {
        return new SysRoleAuthorityService();
    }


    @Bean
    public SysRoleService sysRoleService() {
        return new SysRoleService();
    }


    @Bean
    public SysUserRoleService sysUserRoleService() {
        return new SysUserRoleService();
    }


    @Bean
    public SysUserService sysUserService() {
        return new SysUserService();
    }
}

