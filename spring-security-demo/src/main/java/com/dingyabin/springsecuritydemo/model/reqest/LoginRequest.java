package com.dingyabin.springsecuritydemo.model.reqest;

import lombok.Getter;
import lombok.Setter;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:47
 */
@Getter
@Setter
public class LoginRequest {

    private String userName;

    private String pwd;

}
