package com.dingyabin.localmsg.model.entity;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import com.dingyabin.localmsg.model.common.InvokeContext;
import com.dingyabin.localmsg.model.enums.LocalMsgStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName local_message_record
 */
@Data
@NoArgsConstructor
@TableName(value ="local_message_record")
public class LocalMessageRecord implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String bizType;

    /**
     * 
     */
    private String invokeCtx;

    /**
     * 
     */
    private Integer retryTime;

    /**
     * 
     */
    private Integer maxRetryTime;

    /**
     * 
     */
    private LocalMsgStatusEnum status;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private LocalDateTime createTime;

    /**
     * 
     */
    private LocalDateTime updateTime;


    public LocalMessageRecord(String bizType, InvokeContext invokeContext, Integer maxRetryTime) {
        setBizType(bizType);
        setInvokeCtx(JSON.toJSONString(invokeContext));
        setMaxRetryTime(maxRetryTime);
        setRetryTime(0);
    }

}