package com.dingyabin.web.translate.core.impl;

import com.dingyabin.web.translate.annotation.TranslationType;
import com.dingyabin.web.translate.core.TranslationInterface;
import lombok.AllArgsConstructor;

/**
 * 用户名翻译实现
 *
 * @author Lion Li
 */
@AllArgsConstructor
@TranslationType(type = "test")
public class UserNameTranslationImpl implements TranslationInterface<String> {


    @Override
    public String translation(Object key, String other) {
        return key + "_test";
    }
}
