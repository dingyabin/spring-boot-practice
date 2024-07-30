package com.dingyabin.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author 丁亚宾
 * Date: 2024/7/30.
 * Time:23:22
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class Result<T> {

    private int code = 0;

    private String msg = "请求成功！";

    private T data;

    public Result() {
    }

    public Result(T data) {
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }


    public static <T> Result<T> fail(int code, String msg) {
        return new Result<>(code, msg);
    }
}
