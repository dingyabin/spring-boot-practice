package com.dingyabin.distributeId.vo;

import com.dingyabin.web.translate.annotation.Translation;
import lombok.Data;

import java.io.Serializable;


@Data
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
    private Integer dataCenterId;

    /**
     *
     */
    @Translation(translationType = "test", mapper = "ip")
    private String random;

}