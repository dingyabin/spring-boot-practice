package com.example.springsessiondemo.annotation;

import com.example.springsessiondemo.web.User;
import org.springframework.util.CollectionUtils;

import java.lang.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/6/13.
 * Time:21:31
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface HasRole {

    String[] hasRoles();

    HasRoleCheckType checkType();


    enum HasRoleCheckType {

        HAS_ROLES_AND() {
            @Override
            public boolean doCheck(String[] checkRoles, User currentUser) {
                List<String> hasRoles = currentUser.getRoles();
                return hasRoles != null && hasRoles.containsAll(Arrays.asList(checkRoles));
            }
        },

        HAS_ROLES_OR() {
            @Override
            public boolean doCheck(String[] checkRoles, User currentUser) {
                List<String> hasRoles = currentUser.getRoles();
                return hasRoles != null && CollectionUtils.containsAny(hasRoles, Arrays.asList(checkRoles));
            }
        };

        public abstract boolean doCheck(String[] checkRoles, User currentUser);

    }


}
