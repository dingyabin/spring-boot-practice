package com.dingyabin.sharding.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * @TableName customer
 */
@TableName(value ="customer")
@Data
public class Customer implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    private String cusname;

    /**
     * 
     */
    private LocalDate time;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}