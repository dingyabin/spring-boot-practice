package com.example.springsessiondemo.util;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author 丁亚宾
 * Date: 2024/7/9.
 * Time:23:22
 */
public class HttpService {

    private static final Logger log = LoggerFactory.getLogger(HttpService.class);

    private static Integer timeout = 20000;

    public HttpService() {
    }


    public <T> T postEntity(String url, String body, TypeReference<T> type) {
        return postEntity(url, null, body, type);
    }


    public <T> T postEntity(String url, Map<String, String> headerMap, String body, TypeReference<T> type) {
        String result = "";
        int code = 0;

        try {
            HttpResponse execute = HttpRequest.post(url).headerMap(headerMap, true).setConnectionTimeout(timeout).setReadTimeout(timeout).body(body).execute();
            result = execute.body();
            code = execute.getStatus();
            T sdkResponse = StringUtils.isNotBlank(result) ? JSON.parseObject(result, type) : null;
            if (sdkResponse == null) {
                log.info("{}，请求API返回错误， 请求参数：{}，返回信息：{}", url, body, result);
                return null;
            } else {
                return sdkResponse;
            }
        } catch (Exception var9) {
            log.error("http请求返回WebException,请求连接:{}, 返回异常状态码:{}, 错误信息:{}", url, code, result, var9);
            return null;
        }
    }

    public <T> T getEntity(String url, TypeReference<T> type) {
        String result = "";

        try {
            result = HttpUtil.get(url, timeout);
            T response = StringUtils.isNotBlank(result) ? JSON.parseObject(result, type) : null;
            if (response == null) {
                log.info("请求API返回错误, 请求路径:{}, 返回信息：{}", url, result);
            }

            return response;
        } catch (Exception var5) {
            log.error("http请求返回WebException,请求连接:{}, 错误信息:{}, ", url, result, var5);
            return null;
        }
    }

    public <T> T getEntity(String url, Class<T> clazz) {
        String result = "";

        try {
            result = HttpUtil.get(url, timeout);
            T response = StringUtils.isNotBlank(result) ? JSON.parseObject(result, clazz) : null;
            if (response == null) {
                log.info("请求API返回错误, 请求路径:{}, 返回信息：{}", url, result);
            }

            return response;
        } catch (Exception var5) {
            log.error("http请求返回WebException,请求连接:{}, 错误信息:{}", url, result, var5);
            return null;
        }
    }

    public <T> T getEntity(String url, Map<String, Object> paramMap, TypeReference<T> type) {
        String result = "";

        try {
            result = HttpUtil.get(url, paramMap, timeout);
            T response = StringUtils.isNotBlank(result) ? JSON.parseObject(result, type) : null;
            if (response == null) {
                log.info("请求API返回错误, 请求路径:{}, 请求参数：{}, 返回信息：{}", url, paramMap, result);
            }

            return response;
        } catch (Exception var6) {
            log.error("http请求返回WebException,请求连接:{}, 请求参数：{}, 错误信息:{}", url, paramMap, result, var6);
            return null;
        }
    }

    public <T> T getEntityByHeader(String url, Map<String, String> headerMap, TypeReference<T> type) {
        String result = "";

        try {
            HttpResponse execute = ((HttpRequest) HttpRequest.get(url).headerMap(headerMap, true)).setConnectionTimeout(timeout).setReadTimeout(timeout).execute();
            result = execute.body();
            T returnResponse = StringUtils.isNotBlank(result) ? JSON.parseObject(result, type) : null;
            if (returnResponse == null) {
                log.info("请求API返回错误, 请求路径:{}, header：{}, 返回信息：{}", url, headerMap, result);
            }

            return returnResponse;
        } catch (Exception var7) {
            log.error("http请求返回WebException,请求连接:{}, header：{}, 错误信息:{}", url, headerMap, result, var7);
            return null;
        }
    }

    public String getObject(String url, Map<String, Object> paramMap) {
        String result = "";

        try {
            result = HttpUtil.get(url, paramMap, timeout);
            if (StringUtils.isBlank(result)) {
                log.info("请求API返回错误, 请求路径:{}， 请求参数：{}, 返回信息：{}", url, paramMap, result);
            }

            return result;
        } catch (Exception var5) {
            log.error("http请求返回WebException,请求连接:{}, 请求参数：{}, 错误信息:{}", url, paramMap, result, var5);
            return null;
        }
    }

    public String getObject(String url) {
        String result = "";

        try {
            result = HttpUtil.get(url, timeout);
            if (StringUtils.isBlank(result)) {
                log.info("请求API返回错误, 请求路径:{}, 返回信息：{}", url, result);
            }

            return result;
        } catch (Exception var4) {
            log.error("http请求返回WebException,请求连接:{}, 错误信息:{}", url, result, var4);
            return null;
        }
    }


    public <T> T putEntity(String url, Object body, TypeReference<T> type) {
        String result = "";
        int code = 0;

        try {
            HttpResponse execute = HttpRequest.put(url).setConnectionTimeout(timeout).setReadTimeout(timeout).body(JSON.toJSONString(body)).execute();
            result = execute.body();
            code = execute.getStatus();
            T sdkResponse = StringUtils.isNotBlank(result) ? JSON.parseObject(result, type) : null;
            if (sdkResponse == null) {
                log.info("{}，请求API返回错误， 请求参数：{}，返回信息：{}", url, body, result);
                return null;
            } else {
                return sdkResponse;
            }
        } catch (Exception var8) {
            log.error("http请求返回WebException,请求连接:{}, 返回异常状态码:{}, 错误信息:{}", url, code, result, var8);
            return null;
        }
    }

}
