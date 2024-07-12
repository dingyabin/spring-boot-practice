package com.example.springsessiondemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 测试表
 * @TableName sample_data
 */
@TableName(value ="sample_data")
@Data
public class SampleData implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * xxxxxxx
     */
    private String name;

    /**
     * bbbbbbbbbbbbbb
     */
    private String family;

    /**
     * bbbbbbbbbbbbb
     */
    private String parent;

    /**
     * rrrrrrrrrrrr
     */
    private String mother;

    /**
     * 
     */
    private Date date;

    /**
     * 
     */
    private Integer versions;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;



}