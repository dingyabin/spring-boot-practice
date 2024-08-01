package com.dingyabin.springsecuritydemo.config.security;

import com.dingyabin.security.entity.SysUser;
import com.dingyabin.springsecuritydemo.model.response.SecurityUserCache;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:13:04
 */
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SecurityUserDetails implements UserDetails {

    private SysUser sysUser;

    private List<String> authorityList;


    public SecurityUserDetails(SecurityUserCache securityUserCache) {
        this.sysUser = securityUserCache.getSysUser();
        this.authorityList = securityUserCache.getAuthorityList();
    }


    public SecurityUserDetails(SysUser sysUser, List<String> authorityList) {
        this.sysUser = sysUser;
        this.authorityList = authorityList;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (authorityList == null) {
            return Collections.emptyList();
        }
        return authorityList.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }


    @Override
    public String getPassword() {
        return sysUser.getPwd();
    }

    @Override
    public String getUsername() {
        return sysUser.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return sysUser.getState() > 0;
    }
}
