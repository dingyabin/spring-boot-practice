package com.dingyabin.captcha.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 
 * @TableName image_captcha
 */
@TableName(value ="image_captcha", autoResultMap = true)
@Data
public class ImageCaptcha implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 名字，描述
     */
    private String name;

    /**
     * 图片集合
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> image;

    /**
     * 混淆图片
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private List<String> confuseImg;


    private Integer imageSize;

    /**
     * 是否是混淆图片
     */
    private Integer confuse;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}