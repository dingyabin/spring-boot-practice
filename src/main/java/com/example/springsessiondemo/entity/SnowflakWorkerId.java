package com.example.springsessiondemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName snowflak_worker_id
 */
@TableName(value ="snowflak_worker_id")
@Data
public class SnowflakWorkerId implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String ip;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}