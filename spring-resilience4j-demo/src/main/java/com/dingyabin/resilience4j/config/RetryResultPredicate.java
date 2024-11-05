package com.dingyabin.resilience4j.config;

import com.dingyabin.response.Result;

import java.util.function.Predicate;

/**
 * @author 丁亚宾
 * Date: 2024/11/5.
 * Time:18:40
 */
public class RetryResultPredicate implements Predicate<Result<String>> {

    @Override
    public boolean test(Result<String> o) {
        return false;
    }

}
