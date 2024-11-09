package com.dingyabin.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author Administrator
 * Date: 2024/11/9.
 * Time:16:43
 */
@Getter
@Setter
public class PageResult<T> {

    private int code = 0;

    private String msg = "请求成功！";

    private List<T> data;

    private Page page;


    public PageResult() {
    }

    public PageResult(List<T> data) {
        this.data = data;
    }

    public PageResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public PageResult(int code, String msg, List<T> data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }


    public PageResult(List<T> data, Page page) {
        this(data);
        this.page = page;
    }

    public Page page() {
        this.page = new Page();
        return this.page;
    }


    public PageResult<T> withPage(long curPage, long total) {
        page().setCurPage(curPage).setTotal(total);
        return this;
    }


    public static <T> PageResult<T> success(List<T> data) {
        return new PageResult<>(data);
    }

    public static <T> PageResult<T> success() {
        return new PageResult<>();
    }

    public static <T> PageResult<T> fail(int code, String msg) {
        return new PageResult<>(code, msg);
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    private static class Page {
        /**
         * 总数
         */
        private Long total;
        /**
         * 当前页
         */
        private Long curPage;

    }


}
