package com.example.springsessiondemo.context;

import com.example.springsessiondemo.web.User;

/**
 * @author 丁亚宾
 * Date: 2024/6/13.
 * Time:20:16
 */
public class UserContext {

    private static final ThreadLocal<User> USER_THREADLOCAL = new ThreadLocal<>();


    public static User getCurrentUser() {
        return USER_THREADLOCAL.get();
    }

    public static void setCurrentUser(User user) {
        USER_THREADLOCAL.set(user);
    }

    public static void removeUser() {
        USER_THREADLOCAL.remove();
    }

}
