package com.dingyabin.springsecuritydemo.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:13:06
 */
@Getter
@Setter
public class SysUser implements Serializable {

    private String name;

    private String pwd;

    private int state;

}
