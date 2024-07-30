package com.dingyabin.springsecuritydemo.config.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:0:41
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails xxx = User.builder()
                .username("xxx")
                .password("$2a$10$DD3LVjMCbMUf48rlPqDCq.HwRpTt5qubhJnIy2oOufhWoCS2/79IS")
                .authorities("q")
                .build();
        return xxx;
    }

}
