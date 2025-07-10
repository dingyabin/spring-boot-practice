package com.dingyabin.distributeId.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


@TableName(value ="leaf_alloc")
@Data
public class LeafAlloc implements Serializable {
    /**
     * 
     */
    @TableId
    private String bizTag;

    /**
     * 
     */
    private Long maxId;

    /**
     * 
     */
    private Integer step;

    /**
     * 
     */
    private String description;

    /**
     * 
     */
    private LocalDateTime updateTime;
}