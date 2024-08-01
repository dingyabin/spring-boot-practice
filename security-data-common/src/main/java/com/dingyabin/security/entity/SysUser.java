package com.dingyabin.security.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author 丁亚宾
 * Date: 2024/7/31.
 * Time:13:06
 */
@Getter
@Setter
@ToString
@TableName(value = "sys_user")
public class SysUser implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String pwd;

    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
