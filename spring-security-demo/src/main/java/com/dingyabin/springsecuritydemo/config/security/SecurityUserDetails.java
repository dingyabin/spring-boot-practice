package com.dingyabin.springsecuritydemo.config.security;

import com.dingyabin.springsecuritydemo.entity.SysUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:13:04
 */
@Getter
@Setter
@NoArgsConstructor
public class SecurityUserDetails implements UserDetails {

    private SysUser sysUser;

    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUserDetails(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
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
