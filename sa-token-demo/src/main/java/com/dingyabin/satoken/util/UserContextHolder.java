package com.dingyabin.satoken.util;

import com.dingyabin.satoken.model.LoginUserCache;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class UserContextHolder {

    private static final ThreadLocal<LoginUserCache> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static void setRequestUser(LoginUserCache requestUser) {
        REQUEST_THREAD_LOCAL.set(requestUser);
    }

    public static LoginUserCache getRequestUser() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static Long getRequestUserId() {
        LoginUserCache requestUser = getRequestUser();
        return null == requestUser ? null : requestUser.getUserId();
    }


    public static void remove() {
        REQUEST_THREAD_LOCAL.remove();
    }


}
