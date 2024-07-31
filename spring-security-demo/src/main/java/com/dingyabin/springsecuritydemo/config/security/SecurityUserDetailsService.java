package com.dingyabin.springsecuritydemo.config.security;

import com.dingyabin.springsecuritydemo.entity.SysUser;
import com.dingyabin.springsecuritydemo.service.SysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:0:41
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.loadSysUserByName(username);
        if (sysUser == null) {
            return null;
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        securityUserDetails.setSysUser(sysUser);
        securityUserDetails.setAuthorityList(Collections.singletonList("admin"));
        return securityUserDetails;
    }

}
