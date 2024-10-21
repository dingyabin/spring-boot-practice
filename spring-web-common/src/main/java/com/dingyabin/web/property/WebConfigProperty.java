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

    private boolean autoInjectTraceId = true;

    private boolean logTrace = true;

    private boolean repeatReadRequestProxy = true;

}
