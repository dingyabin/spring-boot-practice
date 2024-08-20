package com.dingyabin.security.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.security.entity.SysUser;
import com.dingyabin.security.mapper.SysUserMapper;

/**
 * @author 丁亚宾
 */
public class SysUserService extends ServiceImpl<SysUserMapper, SysUser> {



    public SysUser loadSysUserByName(String name) {
        LambdaQueryWrapper<SysUser> lambdaQuery = Wrappers.lambdaQuery(SysUser.class);
        lambdaQuery.eq(SysUser::getName, name);
        return getOne(lambdaQuery);
    }


}




