package com.dingyabin.satoken.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:13:06
 */
@Getter
@Setter
@ToString
@Accessors(chain = true)
public class LoginUserCache implements Serializable {

    private Long userId;

    private String name;

    private Integer state;

    private List<String> roleList;

    private List<String> authorityList;

}
