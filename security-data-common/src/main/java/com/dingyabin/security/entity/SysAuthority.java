package com.dingyabin.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName sys_authority
 */
@TableName(value ="sys_authority")
@Data
public class SysAuthority implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    private String authority;

    /**
     * 
     */
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}