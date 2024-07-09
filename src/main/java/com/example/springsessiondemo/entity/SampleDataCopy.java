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
 * @TableName sample_data_copy
 */
@TableName(value ="sample_data_copy")
@Data
public class SampleDataCopy implements Serializable {
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



    public static SampleDataCopy  bySampleData(SampleData sampleData){
        SampleDataCopy sampleDataCopy = new SampleDataCopy();
        sampleDataCopy.setId(sampleData.getId());
        sampleDataCopy.setName(sampleData.getName());
        sampleDataCopy.setFamily(sampleData.getFamily());
        sampleDataCopy.setParent(sampleData.getParent());
        sampleDataCopy.setMother(sampleData.getMother());
        sampleDataCopy.setDate(sampleData.getDate());
        sampleDataCopy.setVersions(sampleData.getVersions());
        return sampleDataCopy;
    }

}