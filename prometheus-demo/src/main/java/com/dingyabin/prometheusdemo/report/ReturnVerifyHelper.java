package com.dingyabin.prometheusdemo.report;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

/**
 * @author 丁亚宾
 * Date: 2024/7/14.
 * Time:23:44
 */
public class ReturnVerifyHelper {

    public static final String OBJECT_NOT_NULL = "OBJECT_NOT_NULL";

    public static final String STR_NOT_NULL = "STR_NOT_NULL";

    private final static Map<String, Predicate<Object>> VERIFY_MAP = new ConcurrentHashMap<>();

    private ReturnVerifyHelper() {
    }

    static {
        VERIFY_MAP.put(OBJECT_NOT_NULL, Objects::nonNull);
        VERIFY_MAP.put(STR_NOT_NULL, o -> (o != null && StringUtils.isNotBlank(o.toString())));
    }


    public static void addVerifyFunction(String key, Predicate<Object> predicate) {
        VERIFY_MAP.put(key, predicate);
    }


    /**
     * 校验方法是否执行成功
     *
     * @param retCheckKey 校验key
     * @param returnValue 方法的返回值
     * @return 是否执行成功
     */
    public static boolean retVerify(String retCheckKey, Object returnValue) {
        Predicate<Object> fun = VERIFY_MAP.get(retCheckKey);
        if (fun == null) {
            return true;
        }
        return fun.test(returnValue);
    }


}
