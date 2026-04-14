package com.dingyabin.config.mybatis;

import com.baomidou.mybatisplus.core.toolkit.Constants;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = MybatisPlusEncryptProperties.ENCRYPT_PREFIX)
public class MybatisPlusEncryptProperties {

    public static final String ENCRYPT_PREFIX = Constants.MYBATIS_PLUS + ".encrypt";

    private Boolean enable;

    private String encryptKey;

}
