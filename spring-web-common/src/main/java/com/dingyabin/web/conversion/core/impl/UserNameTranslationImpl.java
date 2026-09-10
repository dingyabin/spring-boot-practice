package com.dingyabin.web.conversion.core.impl;

import com.dingyabin.web.conversion.core.ConversionInterface;
import lombok.AllArgsConstructor;

/**
 * 用户名翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
public class UserNameTranslationImpl implements ConversionInterface<String> {


    @Override
    public String conversionType() {
        return "test";
    }


    @Override
    public String translation(Object key, String other) {
        return key + "_test";
    }
}
