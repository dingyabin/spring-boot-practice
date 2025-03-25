package com.dingyabin.localmsg.model.enums;

import lombok.Getter;

@Getter
public enum LocalMsgStatusEnum {

    INIT("初始化"),

    RETRY("重试中"),

    FAILED("重试失败"),

    SUCCESS("成功");


    private final String desc;


    LocalMsgStatusEnum(String desc) {
        this.desc = desc;
    }
}
