package com.dingyabin.distributeId.vo;

import com.dingyabin.web.conversion.annotation.Conversion;
import com.dingyabin.web.dynamic.anno.DynamicProperty;
import com.dingyabin.web.dynamic.anno.DynamicPropertyModel;
import lombok.Data;

import java.io.Serializable;


@Data
@DynamicPropertyModel
public class IpWorkerConfigVo implements Serializable {
    /**
     * 
     */
    private String ip = "127.0.0.1";

    /**
     * 
     */
    private Integer workId = 1;

    /**
     * 
     */
    @DynamicProperty
    private Integer dataCenterId;

    /**
     *
     */
    @Conversion(conversionType = "test", mapper = "ip")
    private String random;

}