package com.dingyabin.springsecuritydemo.enums;

import lombok.Getter;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:17:48
 */
@Getter
public enum RedisKeyEnum {

    LOGIN_USER("loginUser:", "string"){
        @Override
        public String toKey(Object... args) {
            return getKeyTemplate() + args[0];
        }
    };


    private final String keyTemplate;


    private final String type;


    RedisKeyEnum(String keyTemplate, String type) {
        this.keyTemplate = keyTemplate;
        this.type = type;
    }

    public abstract String toKey(Object... args);


}
