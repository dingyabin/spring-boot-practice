package com.dingyabin.springsecuritydemo.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:4:17
 */
@Getter
@Setter
public class TokenMsg {

    private long userId;

    private String userName;

}
