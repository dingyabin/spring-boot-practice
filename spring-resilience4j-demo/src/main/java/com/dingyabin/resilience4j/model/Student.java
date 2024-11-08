package com.dingyabin.resilience4j.model;

import com.dingyabin.web.desensization.Desensitization;
import com.dingyabin.web.desensization.enums.DesensitizationTypeEnum;
import lombok.Data;

/**
 * @author 丁亚宾
 * Date: 2024/11/8.
 * Time:15:00
 */
@Data
public class Student {

    @Desensitization(value = DesensitizationTypeEnum.ID_CARD)
    private String name = "adahnfksjghdfkjgh";

    @Desensitization(value = DesensitizationTypeEnum.ID_CARD)
    private String name2 = "adahnfksjghdfkjgh";
}
