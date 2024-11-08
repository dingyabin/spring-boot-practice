package com.dingyabin.web.desensization.enums;

import cn.hutool.core.util.DesensitizedUtil;

import java.util.function.Function;

/**
 * @author 丁亚宾
 * Date: 2024/11/8.
 * Time:14:54
 */
public enum DesensitizedTypeEnum {

    /**
     * 中文名
     */
    CHINESE_NAME(DesensitizedUtil::chineseName),
    /**
     * 身份证号
     */
    ID_CARD(str -> DesensitizedUtil.idCardNum(str, 3, 2)),
    /**
     * 座机号
     */
    FIXED_PHONE(DesensitizedUtil::fixedPhone),
    /**
     * 手机号
     */
    MOBILE_PHONE(DesensitizedUtil::mobilePhone),
    /**
     * 电子邮件
     */
    EMAIL(DesensitizedUtil::email),
    /**
     * 密码
     */
    PASSWORD(DesensitizedUtil::password),
    /**
     * 中国大陆车牌，包含普通车辆、新能源车辆
     */
    CAR_LICENSE(DesensitizedUtil::carLicense),
    /**
     * 银行卡
     */
    BANK_CARD(DesensitizedUtil::bankCard);


    private Function<String, String> desensitize;


    DesensitizedTypeEnum(Function<String, String> desensitize) {
        this.desensitize = desensitize;
    }

    public Function<String, String> getDesensitize() {
        return desensitize;
    }
}
