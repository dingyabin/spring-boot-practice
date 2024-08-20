package com.dingyabin.security.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.security.entity.SysAuthority;
import com.dingyabin.security.mapper.SysAuthorityMapper;

import java.util.List;

/**
 * @author 丁亚宾
 * @description 针对表【sys_authority】的数据库操作Service实现
 * @createDate 2024-07-31 21:17:38
 */
public class SysAuthorityService extends ServiceImpl<SysAuthorityMapper, SysAuthority> {


    public List<SysAuthority> selectAllSysAuthority() {
        return list();
    }


    public List<SysAuthority> selectSysAuthorityByUserId(Long userId){
       return getBaseMapper().selectSysAuthorityByUserId(userId);
    }

}




