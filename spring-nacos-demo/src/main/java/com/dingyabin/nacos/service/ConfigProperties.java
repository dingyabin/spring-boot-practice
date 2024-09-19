package com.dingyabin.nacos.service;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author 丁亚宾
 * Date: 2024/9/19.
 * Time:21:29
 */
@Data
@ToString
@Component
@ConfigurationProperties(prefix = "tom")
public class ConfigProperties {

    private int age;

    private String address;
}
