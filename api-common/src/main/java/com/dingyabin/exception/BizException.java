package com.dingyabin.exception;

/**
 * @author 丁亚宾
 * Date: 2024/11/4.
 * Time:22:42
 */
public class BizException extends RuntimeException {

    public BizException(String message) {
        super(message);
    }

    public BizException(String message, Throwable cause) {
        super(message, cause);
    }

    public BizException(Throwable cause) {
        super(cause);
    }
}
