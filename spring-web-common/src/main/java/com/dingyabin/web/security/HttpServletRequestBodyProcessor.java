package com.dingyabin.web.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author 丁亚宾
 * Date: 2024/10/22.
 * Time:18:20
 */
public interface HttpServletRequestBodyProcessor {


    boolean support(HttpServletRequest request, HttpServletResponse response);

    /**
     * 对原来的body做处理，返回处理之后的body，这个方法会在request读取流之前调用，所以不用
     * 担心读取不到流，而且返回的新body会重新封装到RepeatReadHttpServletRequest中向下传递
     * @param request request
     * @param response response
     * @return 返回处理之后的body
     */
    String doBeforeReadBody(HttpServletRequest request, HttpServletResponse response) throws IOException;

}
