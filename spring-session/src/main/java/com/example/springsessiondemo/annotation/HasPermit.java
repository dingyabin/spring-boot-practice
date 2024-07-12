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
public @interface HasPermit {

    String[] hasPermit();

    HasPermitCheckType checkType();


    enum HasPermitCheckType {

        HAS_PERMIT_AND() {
            @Override
            public boolean doCheck(String[] checkPermits, User currentUser) {
                List<String> userPermits = currentUser.getPermits();
                return userPermits != null && userPermits.containsAll(Arrays.asList(checkPermits));
            }
        },

        HAS_PERMIT_OR() {
            @Override
            public boolean doCheck(String[] checkPermits, User currentUser) {
                List<String> userPermits = currentUser.getPermits();
                return userPermits != null && CollectionUtils.containsAny(userPermits, Arrays.asList(checkPermits));
            }
        };

        public abstract boolean doCheck(String[] checkPermits, User currentUser);

    }


}
