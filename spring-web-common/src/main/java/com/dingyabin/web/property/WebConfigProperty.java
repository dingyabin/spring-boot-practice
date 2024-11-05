package com.dingyabin.web.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author 丁亚宾
 * Date: 2024/10/21.
 * Time:21:41
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "web.common.config")
public class WebConfigProperty {

    //是否自动注入traceId
    private boolean autoInjectTraceId = true;

    //是否输出trace日志
    private boolean logTrace = true;

    //输出的logTrace的前缀
    private String logTracePrefix;

    //是否替换为可重复读的request
    private boolean repeatReadRequestProxy = true;

    //trace日志是否输出ip
    private boolean printIp = true;

    //trace日志是否输出header
    private boolean printHeader = false;


}
