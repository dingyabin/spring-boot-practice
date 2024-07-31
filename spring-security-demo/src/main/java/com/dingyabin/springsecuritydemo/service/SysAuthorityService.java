package com.dingyabin.springsecuritydemo.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dingyabin.springsecuritydemo.entity.SysAuthority;
import com.dingyabin.springsecuritydemo.mapper.SysAuthorityMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 丁亚宾
 * @description 针对表【sys_authority】的数据库操作Service实现
 * @createDate 2024-07-31 21:17:38
 */
@Service
public class SysAuthorityService extends ServiceImpl<SysAuthorityMapper, SysAuthority> {


    public List<SysAuthority> selectAllSysAuthority() {
        return list();
    }


    public List<SysAuthority> selectSysAuthorityByUserId(Long userId){
       return getBaseMapper().selectSysAuthorityByUserId(userId);
    }

}




