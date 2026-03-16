package com.dingyabin.web.conversion.core.impl;

import com.dingyabin.web.conversion.annotation.ConversionType;
import com.dingyabin.web.conversion.core.ConversionInterface;
import lombok.AllArgsConstructor;

/**
 * 用户名翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@ConversionType(type = "test")
public class UserNameTranslationImpl implements ConversionInterface<String> {


    @Override
    public String translation(Object key, String other) {
        return key + "_test";
    }
}
