package com.dingyabin.util;

import com.baomidou.mybatisplus.core.toolkit.AES;
import org.apache.commons.lang3.StringUtils;

public class EncryptHelper {

    public static final String ENCODE_PREFIX = "#ENCODE#";


    /**
     * 加密
     * @param data 明文
     * @param key 秘钥
     * @return 结果
     */
    public static String encrypt(String data, String key) {
        return ENCODE_PREFIX + AES.encrypt(data, key);
    }

    /**
     * 是否是加密过的字符串
     *
     * @param data 待检查的字符串
     * @return 是否
     */
    public static boolean isEncrypt(String data) {
        return data.startsWith(EncryptHelper.ENCODE_PREFIX);
    }

    /**
     * 解密
     * @param data 秘文
     * @param key 秘钥
     * @return 结果
     */
    public static String decrypt(String data, String key) {
        data = StringUtils.substringAfter(data, EncryptHelper.ENCODE_PREFIX);
        return AES.decrypt(data, key);
    }

}
