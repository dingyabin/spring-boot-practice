package com.dingyabin.springsecuritydemo.model.response;

import com.dingyabin.springsecuritydemo.config.security.SecurityUserDetails;
import com.dingyabin.springsecuritydemo.entity.SysUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:17:35
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityUserCache {

    private SysUser sysUser;

    private List<String> authorityList;


    public SecurityUserCache(SecurityUserDetails securityUserDetails) {
        this.sysUser = securityUserDetails.getSysUser();
        this.authorityList = securityUserDetails.getAuthorityList();
    }


    public SecurityUserCache clearPwd() {
        if (sysUser != null) {
            sysUser.setPwd(null);
        }
        return this;
    }

}
