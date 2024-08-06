package com.dingyabin.satoken.config;

import cn.dev33.satoken.stp.StpInterface;
import com.dingyabin.satoken.model.LoginUserCache;
import com.dingyabin.satoken.util.UserContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/8/7.
 * Time:0:43
 */
@Component
public class StpInterfaceImpl implements StpInterface {

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        LoginUserCache requestUser = UserContextHolder.getRequestUser();
        if (requestUser != null) {
            return requestUser.getAuthorityList();
        }
        return Collections.emptyList();
    }


    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询角色
        LoginUserCache requestUser = UserContextHolder.getRequestUser();
        if (requestUser != null) {
            return requestUser.getRoleList();
        }
        return Collections.emptyList();
    }
}
