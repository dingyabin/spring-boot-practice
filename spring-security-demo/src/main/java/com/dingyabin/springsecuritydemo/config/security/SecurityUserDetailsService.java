package com.dingyabin.springsecuritydemo.config.security;

import com.dingyabin.security.entity.SysAuthority;
import com.dingyabin.security.entity.SysUser;
import com.dingyabin.security.service.SysAuthorityService;
import com.dingyabin.security.service.SysUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:0:41
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysAuthorityService sysAuthorityService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.loadSysUserByName(username);
        if (sysUser == null) {
            return null;
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails();
        securityUserDetails.setSysUser(sysUser);

        List<SysAuthority> sysAuthorities = sysAuthorityService.selectSysAuthorityByUserId(sysUser.getId());
        securityUserDetails.setAuthorityList(sysAuthorities.stream().map(SysAuthority::getAuthority).collect(Collectors.toList()));
        return securityUserDetails;
    }

}
