package com.dingyabin.springsecuritydemo.model.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:4:17
 */
@Getter
@Setter
@NoArgsConstructor
public class TokenMsg {

    private Long userId;

    private String userName;


    public TokenMsg(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public TokenMsg(Long userId) {
        this.userId = userId;
    }

    public TokenMsg(String userName) {
        this.userName = userName;
    }
}
